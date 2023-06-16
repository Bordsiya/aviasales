//package com.example.mailservice.client;
//
//import java.util.List;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@FeignClient(
//        name = "ack-client",
//        url = "http://localhost:9095"
//)
//public interface AckClient {
//    @PostMapping("/recommendation/{userId}")
//    List<RecommendationDto> getAllUserRecommendations(@PathVariable long userId);
//}
