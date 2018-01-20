package br.com.cedran.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("externalApi")
public interface ExternalApi {

    @RequestMapping(method = RequestMethod.GET, value = "/name/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ExternalResponseDTO searchName(@PathVariable("id") Long id);

}
