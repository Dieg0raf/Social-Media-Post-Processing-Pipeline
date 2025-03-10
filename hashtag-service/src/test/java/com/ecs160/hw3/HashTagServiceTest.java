package com.ecs160.hw3;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.response.OllamaResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.contains;

@WebMvcTest(HashTagController.class)
public class HashTagServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OllamaAPI ollamaAPI;

	@Test
	public void testHashTagExtraction() throws Exception {
		// Mock the OllamaAPI response
		OllamaResult mockResult = mock(OllamaResult.class);
		when(mockResult.getResponse()).thenReturn("#awesome");
		when(ollamaAPI.generate(anyString(), anyString(), anyBoolean(), any())).thenReturn(mockResult);

		// Test the controller
		mockMvc.perform(post("/hash-tag")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This is an awesome post\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#awesome"));
	}

	@Test
	public void testEmptyContent() throws Exception {
		// Test with empty content
		mockMvc.perform(post("/hash-tag")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"\"}"))
				.andExpect(status().isOk()); // Controller doesn't return 400 for empty content

		// Verify OllamaAPI was still called (with empty content)
		verify(ollamaAPI, times(1)).generate(anyString(), contains("Generate exactly one hashtag for this post:"),
				anyBoolean(), any());
	}

	@Test
	public void testNoHashtags() throws Exception {
		// Mock the OllamaAPI to return a non-hashtag response
		OllamaResult mockResult = mock(OllamaResult.class);
		when(mockResult.getResponse()).thenReturn("This is not a hashtag");
		when(ollamaAPI.generate(anyString(), anyString(), anyBoolean(), any())).thenReturn(mockResult);

		// Test the controller
		mockMvc.perform(post("/hash-tag")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This post has no hashtags\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost")); // Should return the default hashtag
	}

	@Test
	public void testOllamaAPIException() throws Exception {
		// Mock the OllamaAPI to throw an exception
		when(ollamaAPI.generate(anyString(), anyString(), anyBoolean(), any()))
				.thenThrow(new RuntimeException("API Error"));

		// Test the controller
		mockMvc.perform(post("/hash-tag")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This should cause an error\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost")); // Should return the default hashtag
	}
}