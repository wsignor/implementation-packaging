package com.mobiquityinc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiquityinc.constants.TestConstants;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import java.util.StringJoiner;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PackageController.class)
public class PackageControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    String filePath;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() {
        filePath = getClass().getClassLoader().getResource("things.txt").getPath();
    }

    @Test
    public void packThings_whenHappyPath_shouldReturnThings() throws Exception {

        StringJoiner compare = new StringJoiner("\n");
        compare.add("4");
        compare.add("-");
        compare.add("2,7");
        compare.add("8,9");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestConstants.PACKAGE_URI)
                .param("filePath", filePath)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        Assert.assertEquals(compare.toString(), mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void packThings_whenParamNotProvided_shouldReturnError() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(TestConstants.PACKAGE_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400)).andReturn();
    }

    @Test(expected = NestedServletException.class)
    public void packThings_whenFileDoesntExist_shouldThrowException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(TestConstants.PACKAGE_URI)
                .param("filePath", "nonExistentPath")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
    }

    @Test(expected = NestedServletException.class)
    public void packThings_whenInvalidThings_shouldReturnError() throws Exception {
        String thingsWithInvalidFormat = getClass().getClassLoader().getResource("things-invalid-format.txt").getPath();

        mockMvc.perform(MockMvcRequestBuilders.get(TestConstants.PACKAGE_URI)
                .param("filePath", thingsWithInvalidFormat)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
    }

    @Test
    public void packThings_whenEmptyFile_shouldReturnError() throws Exception {
        String emptyFile = getClass().getClassLoader().getResource("things-empty.txt").getPath();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestConstants.PACKAGE_URI)
                .param("filePath", emptyFile)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        Assert.assertEquals(StringUtils.EMPTY, mvcResult.getResponse().getContentAsString());
    }
}
