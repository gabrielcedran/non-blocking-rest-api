package br.com.cedran.web;

import br.com.cedran.dto.RequestDTO;
import br.com.cedran.dto.ResponseDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Timer;
import java.util.TimerTask;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {


    @RequestMapping(
            value = "/blocking-endpoint",
            method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @SneakyThrows
    public ResponseDTO blockingEndpoint(@RequestBody RequestDTO requestDTO) {
        log.info("Started processing request {}", requestDTO.getName());
        Thread.sleep(5000);
        log.info("Ended processing request {}", requestDTO.getName());
        return new ResponseDTO(requestDTO.getName(), "Blocking endpoint");
    }

    @RequestMapping(
            value = "/nonblocking-endpoint",
            method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public DeferredResult<ResponseDTO> nonBlockingEndpoint(@RequestBody RequestDTO requestDTO) {
        DeferredResult<ResponseDTO> deferredResult = new DeferredResult<ResponseDTO>();
        log.info("Started processing request {}", requestDTO.getName());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            @SneakyThrows
            public void run() {
                Thread.sleep(5000);
                log.info("Finished runnable {}", requestDTO.getName());
                deferredResult.setResult(new ResponseDTO(requestDTO.getName(), "Non blocking endpoint"));
            }
        }, 0l);

        log.info("Ended processing request {}", requestDTO.getName());
        return deferredResult;
    }
}
