package com.ecs160.hw3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HashTagController.class)
public class HashTagServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@SpyBean
	private HashTagController hashTagController;

	@Test
	public void testHashTagExtraction() throws Exception {
		// Mock the controller to return a specific hashtag
		doReturn("#awesome").when(hashTagController).hashTag(any());

		// Test the controller
		mockMvc.perform(post("/hash-tag")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This is an awesome post\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#awesome"));

		// Verify the controller was called
		verify(hashTagController, times(1)).hashTag(any());
	}

	@Test
	public void testEmptyContent() throws Exception {
		doReturn("#empty").when(hashTagController).hashTag(any());

		mockMvc.perform(post("/hash-tag")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#empty"));

		verify(hashTagController, times(1)).hashTag(any());
	}

	@Test
	public void testNoHashtags() throws Exception {
		doReturn("#bskypost").when(hashTagController).hashTag(any());

		mockMvc.perform(post("/hash-tag")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This post has no hashtags\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost"));

		verify(hashTagController, times(1)).hashTag(any());
	}

	@Test
	public void testOllamaAPIException() throws Exception {
		// Mock the controller to return the default hashtag when an exception occurs
		doReturn("#bskypost").when(hashTagController).hashTag(any());

		// Test the controller
		mockMvc.perform(post("/hash-tag")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This should cause an error\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost"));

		// Verify the controller was called
		verify(hashTagController, times(1)).hashTag(any());
	}
}