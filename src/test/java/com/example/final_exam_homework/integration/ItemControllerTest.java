package com.example.final_exam_homework.integration;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:controller_insert_users.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:controller_delete_users.sql")
@Import(WebSecurityConfiguration.class)
@ActiveProfiles(profiles = "test")
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String jwt;

    @BeforeEach
    public void authenticate() throws Exception {
        String userObject =
                new JSONObject().put("username", "Zeno Petceran").put("password", "12345678").toString();
        MvcResult result =
                mockMvc
                        .perform(
                                MockMvcRequestBuilders.post("/login")
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(userObject))
                        .andReturn();
        jwt =
                result
                        .getResponse()
                        .getContentAsString()
                        .substring(22, result.getResponse().getContentAsString().length() - 23);
    }

    @Test
    public void createForSaleItemResponse_WithInvalidInformation_ShouldReturnOkAndItemInformation() throws Exception {

        String forSaleItemInformation = new JSONObject()
                .put("name", "BATMAN KNIGHTWATCH #1")
                .put("description", "After a massive breakout at Arkham Asylum, Batman and his team are on a mission to bring all the escapees back.")
                .put("photo_url", "https://davescomicshop.com/wp-content/uploads/2022/07/0722DC091.jpg")
                .put("starting_price", "129")
                .put("purchase_price", "1299")
                .toString();

        mockMvc
                .perform(post("/sell").content(forSaleItemInformation).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("BATMAN KNIGHTWATCH #1")))
                .andExpect(jsonPath("$.description", is("After a massive breakout at Arkham Asylum, Batman and his team are on a mission to bring all the escapees back.")))
                .andExpect(jsonPath("$.photo_url", is("https://davescomicshop.com/wp-content/uploads/2022/07/0722DC091.jpg")))
                .andExpect(jsonPath("$.starting_price", is(129)))
                .andExpect(jsonPath("$.purchase_price", is(1299)));
    }

    @Test
    public void createForSaleItemResponse_WithInvalidInformation_ShouldReturnMissingNameException() throws Exception {

        String forSaleItemInformation = new JSONObject()
                .put("name", "")
                .put("description", "After a massive breakout at Arkham Asylum, Batman and his team are on a mission to bring all the escapees back.")
                .put("photo_url", "https://davescomicshop.com/wp-content/uploads/2022/07/0722DC091.jpg")
                .put("starting_price", "129")
                .put("purchase_price", "1299")
                .toString();

        mockMvc
                .perform(post("/sell").content(forSaleItemInformation).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Name is missing!")));
    }

    @Test
    public void createForSaleItemResponse_WithInvalidInformation_ShouldReturnMissingDescriptionException() throws Exception {

        String forSaleItemInformation = new JSONObject()
                .put("name", "BATMAN KNIGHTWATCH #1")
                .put("description", "")
                .put("photo_url", "https://davescomicshop.com/wp-content/uploads/2022/07/0722DC091.jpg")
                .put("starting_price", "129")
                .put("purchase_price", "1299")
                .toString();

        mockMvc
                .perform(post("/sell").content(forSaleItemInformation).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Description is missing!")));
    }

    @Test
    public void createForSaleItemResponse_WithInvalidInformation_ShouldReturnMissingPhotoUrlException() throws Exception {

        String forSaleItemInformation = new JSONObject()
                .put("name", "BATMAN KNIGHTWATCH #1")
                .put("description", "After a massive breakout at Arkham Asylum, Batman and his team are on a mission to bring all the escapees back.")
                .put("photo_url", "")
                .put("starting_price", "129")
                .put("purchase_price", "1299")
                .toString();

        mockMvc
                .perform(post("/sell").content(forSaleItemInformation).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Photo Url is missing!")));
    }

    @Test
    public void createForSaleItemResponse_WithInvalidInformation_ShouldReturnInvalidPhotoUrlException() throws Exception {

        String forSaleItemInformation = new JSONObject()
                .put("name", "BATMAN KNIGHTWATCH #1")
                .put("description", "After a massive breakout at Arkham Asylum, Batman and his team are on a mission to bring all the escapees back.")
                .put("photo_url", "davescomicshop.com/wp-content/uploads/2022/07/0722DC091.jpg")
                .put("starting_price", "129")
                .put("purchase_price", "1299")
                .toString();

        mockMvc
                .perform(post("/sell").content(forSaleItemInformation).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Photo Url is invalid!")));
    }

    @Test
    public void createForSaleItemResponse_WithInvalidStartingPrice_ShouldReturnInvalidPriceException() throws Exception {

        String forSaleItemInformation = new JSONObject()
                .put("name", "BATMAN KNIGHTWATCH #1")
                .put("description", "After a massive breakout at Arkham Asylum, Batman and his team are on a mission to bring all the escapees back.")
                .put("photo_url", "https://davescomicshop.com/wp-content/uploads/2022/07/0722DC091.jpg")
                .put("starting_price", "-1")
                .put("purchase_price", "1299")
                .toString();

        mockMvc
                .perform(post("/sell").content(forSaleItemInformation).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Given price is invalid!")));
    }

    @Test
    public void createForSaleItemResponse_WithInvalidPurchasePrice_ShouldReturnInvalidPriceException() throws Exception {

        String forSaleItemInformation = new JSONObject()
                .put("name", "BATMAN KNIGHTWATCH #1")
                .put("description", "After a massive breakout at Arkham Asylum, Batman and his team are on a mission to bring all the escapees back.")
                .put("photo_url", "https://davescomicshop.com/wp-content/uploads/2022/07/0722DC091.jpg")
                .put("starting_price", "129")
                .put("purchase_price", "-1")
                .toString();

        mockMvc
                .perform(post("/sell").content(forSaleItemInformation).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Given price is invalid!")));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:controller_insert_items.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:controller_delete_items.sql")
    public void listForSaleItems_WithValidInput_ShouldReturnListOfForSaleItems() throws Exception {
        mockMvc
                .perform(get("/list?page=0").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(20)))
                .andExpect(jsonPath("$.[0].name", is("ACTION COMICS #1047")))
                .andExpect(jsonPath("$.[0].photo_url", is("https://davescomicshop.com/wp-content/uploads/2022/07/0722DC064-scaled.jpg")))
                .andExpect(jsonPath("$.[0].last_bid", is(219)))
                .andExpect(jsonPath("$.[19].name", is("BATMAN SUPERMAN WORLDS FINEST #4")))
                .andExpect(jsonPath("$.[19].photo_url", is("https://davescomicshop.com/wp-content/uploads/2022/07/STL230652.jpg")))
                .andExpect(jsonPath("$.[19].last_bid", is(159)));
    }

    @Test
    public void listForSaleItems_WithInvalidInput_ShouldReturnInvalidPageException() throws Exception {
        mockMvc
                .perform(get("/list?page=-1").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }
}
