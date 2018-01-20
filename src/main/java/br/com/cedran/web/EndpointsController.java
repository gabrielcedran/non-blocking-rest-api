package br.com.cedran.web;

import br.com.cedran.client.ExternalApi;
import br.com.cedran.client.ExternalResponseDTO;
import br.com.cedran.dto.ResponseDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.websocket.server.PathParam;
import java.util.Timer;
import java.util.TimerTask;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class EndpointsController {

    @Autowired
    private ExternalApi externalApi;

    @RequestMapping(
            value = "/blocking-endpoint/{id}",
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseDTO blockingEndpoint(@PathVariable("id") Long id) {
        log.info("Started processing request {}", id);
        ExternalResponseDTO externalResponseDTO = externalApi.searchName(id);
        log.info("Ended processing request {}", id);
        return new ResponseDTO(externalResponseDTO.getName(), "Blocking endpoint");
    }

    @RequestMapping(
            value = "/nonblocking-endpoint",
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE
    )
    public DeferredResult<ResponseDTO> nonBlockingEndpoint(@PathParam("id") Long id) {
        DeferredResult<ResponseDTO> deferredResult = new DeferredResult<ResponseDTO>();
        log.info("Started processing request {}", id);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            @SneakyThrows
            public void run() {
                ExternalResponseDTO externalResponseDTO = externalApi.searchName(id);
                log.info("Finished runnable {}", externalResponseDTO.getName());
                deferredResult.setResult(new ResponseDTO(externalResponseDTO.getName(), "Non blocking endpoint"));
            }
        }, 0l);

        log.info("Ended processing request {}", id);
        return deferredResult;
    }
}
