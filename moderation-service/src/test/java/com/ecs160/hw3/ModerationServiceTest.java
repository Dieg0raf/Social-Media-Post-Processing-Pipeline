package com.ecs160.hw3;

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

@WebMvcTest(ModerationController.class)
public class ModerationServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ModerationHttpClient httpClientService;

	@Test
	public void testModerationPass() throws Exception {
		// Mock the HTTP client to return a response
		when(httpClientService.sendRequest(anyString())).thenReturn("#bskypost");

		// Test the controller with safe content (no banned words)
		mockMvc.perform(post("/moderate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This is a safe post\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost"));

		// Verify HTTP client was called
		verify(httpClientService, times(1)).sendRequest(anyString());
	}

	@Test
	public void testModerationFail() throws Exception {
		// Test with content containing a banned word
		mockMvc.perform(post("/moderate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This post contains illegal content\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("FAILED"));

		// Verify HTTP client was NOT called
		verify(httpClientService, never()).sendRequest(anyString());
	}

	@Test
	public void testEmptyContent() throws Exception {
		// Mock the HTTP client to return a response for empty content
		when(httpClientService.sendRequest(anyString())).thenReturn("#bskypost");

		// Test with empty content
		mockMvc.perform(post("/moderate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost"));

		// Empty content should pass moderation and call the HTTP client
		verify(httpClientService, times(1)).sendRequest(anyString());
	}

	@Test
	public void testHashtagServiceUnavailable() throws Exception {
		// Mock the HTTP client to throw an exception
		when(httpClientService.sendRequest(anyString())).thenReturn("#bskypost");

		// Test the controller with safe content
		mockMvc.perform(post("/moderate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This is a safe post\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost"));

		// Verify HTTP client was called
		verify(httpClientService, times(1)).sendRequest(anyString());
	}
}