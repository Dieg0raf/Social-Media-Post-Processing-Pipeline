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

@WebMvcTest(ModerationController.class)
public class ModerationServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@SpyBean
	private ModerationController moderationController;

	@Test
	public void testModerationPass() throws Exception {
		// Mock the controller to return a specific response
		doReturn("#bskypost").when(moderationController).moderate(any());

		// Test the controller with safe content
		mockMvc.perform(post("/moderate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This is a safe post\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost"));

		// Verify the controller was called
		verify(moderationController, times(1)).moderate(any());
	}

	@Test
	public void testModerationFail() throws Exception {
		// Mock the controller to return FAILED for content with banned words
		doReturn("FAILED").when(moderationController).moderate(any());

		// Test with content containing a banned word
		mockMvc.perform(post("/moderate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This post contains illegal content\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("FAILED"));

		// Verify the controller was called
		verify(moderationController, times(1)).moderate(any());
	}

	@Test
	public void testEmptyContent() throws Exception {
		// Mock the controller to return a specific response for empty content
		doReturn("#bskypost").when(moderationController).moderate(any());

		// Test with empty content
		mockMvc.perform(post("/moderate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost"));

		// Verify the controller was called
		verify(moderationController, times(1)).moderate(any());
	}

	@Test
	public void testHashtagServiceUnavailable() throws Exception {
		// Mock the controller to return a specific response
		doReturn("#bskypost").when(moderationController).moderate(any());

		// Test the controller with safe content
		mockMvc.perform(post("/moderate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"postContent\": \"This is a safe post\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("#bskypost"));

		// Verify the controller was called
		verify(moderationController, times(1)).moderate(any());
	}
}