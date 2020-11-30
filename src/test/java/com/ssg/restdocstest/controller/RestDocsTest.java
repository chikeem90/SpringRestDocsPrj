package com.ssg.restdocstest.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class RestDocsTest {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
	
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)) 
				.build();
	}

	// cars 전체를 반환.
	@Test
	public void testCars() throws Exception {
		this.mockMvc.perform(get("/cars")) 
		.andExpect(status().isOk()) 
		// request 또는 response 결과에 대해 전처리해주는 preprocessor
		// prettyPrint()를 적용하면 결과가 깔끔하게 정리되서 snippet으로 작성된다.
		// 이외에도 request 또는 response의 특정 header 값을 제외 처리해주거나,
		// parameter 값을 추가하거나 제외할 수 있음.
		.andDo(document("cars",  preprocessResponse(prettyPrint())));
	}
	
	// brand가 KIA인 car를 반환.
	@Test
	public void testCarsWithBrand() throws Exception {
		this.mockMvc.perform(get("/cars/{brand}", "KIA")) 
		.andExpect(status().isOk()) 
		// 넘겨주는 파라미터에 대해 설명해주는 snippet을 생성하기 위해서는 이런 식으로 작성.
		.andDo(document("carsWithBrand", pathParameters(
				parameterWithName("brand").description("The car's brand"))));
	}
	
	@Test
	public void testCarsWithId() throws Exception {
		this.mockMvc.perform(get("/cars?id=1")) 
		.andExpect(status().isOk()) 
		// 넘겨주는 파라미터에 대해 설명해주는 snippet을 생성하기 위해서는 이렇게 작성.
		.andDo(document("carsWithId", requestParameters(
				parameterWithName("id").description("The car's id"))));
	}
	
	@Test
	public void testCarsWithResponse() throws Exception {
		this.mockMvc.perform(get("/cars")) 
		.andExpect(status().isOk()) 
		.andDo(document("carsWithResponse", responseFields(
				// field가 하나라도 없으면 error 발생..
				fieldWithPath("[].carId").description("The car's id"),
				fieldWithPath("[].brand").description("The car's brand"),
				fieldWithPath("[].name").description("The car's name"),
				fieldWithPath("[].links").description("Links"))));
		
	}
	
	@Test
	public void testCarsHeader() throws Exception {
		// request header에 값을 set해서 보냄.
		this.mockMvc.perform(get("/cars").header("Authorization", "Basic dXNlcjpzZWNyZXQ=")) 
		.andExpect(status().isOk()) 
		// response header에 대해 설명해주는 snippet을 생성하기 위해서는 이렇게 작성.
		.andDo(document("carsHeader",
				requestHeaders( 
					headerWithName("Authorization").description(
							"Basic auth credentials")), 
				responseHeaders( 
					headerWithName("testHeaderValue").description(
							"Test header value"))));
	}
	
	@Test
	public void testCarsWithLinks() throws Exception {
		this.mockMvc.perform(get("/carsWithLinks")) 
		.andExpect(status().isOk()) 
		// response에 link도 같이 담겨 오는 경우, 아래와 같이 작성할 경우 해당 link에 대해 설명해주는 snippet 생성.
		.andDo(document("carsWithLinks", links(
				linkWithRel("self").description("Link to Self"))));
	}

}
