package cip.interview.passmanagement.web;

import cip.interview.passmanagement.PassTestHelper;
import cip.interview.passmanagement.exceptions.InvalidPassException;
import cip.interview.passmanagement.exceptions.PassAlreadyExistsException;
import cip.interview.passmanagement.exceptions.PassNotFoundException;
import cip.interview.passmanagement.service.PassService;
import com.fasterxml.jackson.databind.ObjectMapper;
import cip.interview.passmanagement.domain.Pass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PassController.class)
public class PassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassService service;

    private PassRequest passRequest = PassTestHelper.validPassRequest();

    @Test
    public void shouldCreateValidPass() throws Exception {
        createPass()
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldReturnBadRequestWhenCreateInvalidPass() throws Exception {
        Mockito.doThrow(new InvalidPassException("pass failed validation")).when(service).create(any(Pass.class));

        createPass()
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldReturnConflictWhenCreateAlreadyExistingPass() throws Exception {
        Mockito.doThrow(new PassAlreadyExistsException()).when(service).create(any(Pass.class));

        createPass()
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldConvertPassRequestToPassModelWhenCreatePass() throws Exception {
        ArgumentCaptor<Pass> passArgument = ArgumentCaptor.forClass(Pass.class);

        createPass();

        verify(service).create(passArgument.capture());
        Pass pass = passArgument.getValue();

        assertThat(pass.getVendorId()).isEqualTo(PassTestHelper.VENDOR_ID);
        assertThat(pass.getPassId()).isEqualTo(PassTestHelper.PASS_ID);
        assertThat(pass.getCustomerName()).isEqualTo(passRequest.getCustomerName());
        assertThat(pass.getHomeCity()).isEqualTo(passRequest.getHomeCity());
        assertThat(pass.getPassCity()).isEqualTo(passRequest.getPassCity());
        assertThat(pass.getPassLength()).isEqualTo(passRequest.getPassLength());
    }

    @Test
    public void shouldCancelPass() throws Exception {

        cancelPass()
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldReturnNotFoundWhenCancelUnknownPass() throws Exception {
        Mockito.doThrow(new PassNotFoundException()).when(service).cancel(PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID);

        cancelPass()
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldRenewPass() throws Exception {

        renewPass()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldReturnNotFoundWhenRenewUnknownPass() throws Exception {
        doThrow(new PassNotFoundException()).when(service).renew(PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID, PassTestHelper.PASS_LENGTH);

        renewPass()
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldCheckIsActive() throws Exception {
        given(service.isActive(PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID)).willReturn(true);

        isActivePass()
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        given(service.isActive(PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID)).willReturn(false);

        isActivePass()
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void shouldReturnNotFoundWhenCheckIsActiveForUnknownPass() throws Exception {
        doThrow(new PassNotFoundException()).when(service).isActive(PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID);

        isActivePass()
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    private ResultActions createPass() throws Exception {
        return mockMvc.perform(post("/vendors/{vendorId}/passes/{passId}", PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID)
                .content(asJsonString(passRequest))
                .contentType(APPLICATION_JSON_UTF8));
    }

    private ResultActions cancelPass() throws Exception {
        return mockMvc.perform(delete("/vendors/{vendorId}/passes/{passId}", PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID));
    }

    private ResultActions renewPass() throws Exception {
        return mockMvc.perform(put("/vendors/{vendorId}/passes/{passId}/renew", PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID)
                .content(PassTestHelper.PASS_LENGTH.toString())
                .contentType(APPLICATION_JSON_UTF8));
    }

    private ResultActions isActivePass() throws Exception {
        return mockMvc.perform(get("/vendors/{vendorId}/passes/{passId}/is-active", PassTestHelper.VENDOR_ID, PassTestHelper.PASS_ID));
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
