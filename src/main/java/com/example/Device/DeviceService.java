package com.example.Device;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeviceService {
  //  @Autowired
    private final DeviceRepository deviceRepository;
    private final RestTemplate restTemplate;

   // @Autowired
  //  private RestTemplate restTemplate;
  //  @Autowired
   // private final WebClient webClient;

//    public DeviceService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://localhost:8000/your-python-endpoint").build();
//    }
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    public Device addDevice(Device device) {

        return deviceRepository.save(device);
    }
    public String getGreeting() {
        String url = "http://127.0.0.1:5000/get";
        return restTemplate.getForObject(url, String.class);
    }
    public Device predictPrice(Long deviceId) {
        Device device = getDeviceById(deviceId);
        if (device == null) {
            return null; // Or throw an exception
        }

        // Prepare the device data to be sent to the Python API
        String url = "https://237b-34-23-161-162.ngrok-free.app/predict";
        Map<String, Object> deviceMap = new HashMap<>();
        deviceMap.put("battery_power", device.getBatteryPower());
        deviceMap.put("blue", device.getBlue());
        deviceMap.put("clock_speed", device.getClockSpeed());
        deviceMap.put("dual_sim", device.getDualSim());
        deviceMap.put("fc", device.getFc());
        deviceMap.put("four_g", device.getFourG());
        deviceMap.put("int_memory", device.getIntMemory());
        deviceMap.put("m_dep", device.getMDep());
        deviceMap.put("mobile_wt", device.getMobileWt());
        deviceMap.put("n_cores", device.getNCores());
        deviceMap.put("pc", device.getPc());
        deviceMap.put("px_height", device.getPxHeight());
        deviceMap.put("px_width", device.getPxWidth());
        deviceMap.put("ram", device.getRam());
        deviceMap.put("sc_h", device.getScH());
        deviceMap.put("sc_w", device.getScW());
        deviceMap.put("talk_time", device.getTalkTime());
        deviceMap.put("three_g", device.getThreeG());
        deviceMap.put("touch_screen", device.getTouchScreen());
        deviceMap.put("wifi", device.getWifi());

        // Call the Python API to get the prediction
        Map<String, Integer> response = restTemplate.postForObject(url, deviceMap, Map.class);
        if (response != null && response.containsKey("price_range")) {
            int predictedPriceRange = response.get("price_range");
            device.setPriceRange(predictedPriceRange);
            return deviceRepository.save(device);
        } else {
            return null; // Or handle the error appropriately
        }
    }
//    public Device callPythonEndpoint(Long deviceId) {
//        Device device = getDeviceById(deviceId);
//        if (device == null) {
//            return null; // Or throw an exception
//        }
//
//        // Prepare the device data to be sent to the Python API
//        String url = "http://localhost:5000/predict";
//        Map<String, Object> deviceMap = new HashMap<>();
//        deviceMap.put("battery_power", device.getBatteryPower());
//        deviceMap.put("blue", device.getBlue());
//        deviceMap.put("clock_speed", device.getClockSpeed());
//        deviceMap.put("dual_sim", device.getDualSim());
//        deviceMap.put("fc", device.getFc());
//        deviceMap.put("four_g", device.getFourG());
//        deviceMap.put("int_memory", device.getIntMemory());
//        deviceMap.put("m_dep", device.getMDep());
//        deviceMap.put("mobile_wt", device.getMobileWt());
//        deviceMap.put("n_cores", device.getNCores());
//        deviceMap.put("pc", device.getPc());
//        deviceMap.put("px_height", device.getPxHeight());
//        deviceMap.put("px_width", device.getPxWidth());
//        deviceMap.put("ram", device.getRam());
//        deviceMap.put("sc_h", device.getScH());
//        deviceMap.put("sc_w", device.getScW());
//        deviceMap.put("talk_time", device.getTalkTime());
//        deviceMap.put("three_g", device.getThreeG());
//        deviceMap.put("touch_screen", device.getTouchScreen());
//        deviceMap.put("wifi", device.getWifi());
//
//
//
////        if (response != null && response.containsKey("price_range")) {
////            int predictedPriceRange = response.get("price_range");
////            device.setPriceRange(predictedPriceRange);
//
//        Mono<Device> responseMono = webClient.post()
//                .bodyValue(deviceMap)
//                .retrieve()
//                .bodyToMono(Device.class);
//
//
//        return  deviceRepository.save(device);
//    }
//    public String callPythonEndpoint(Device data) {
//
//        Mono<String> responseMono = webClient.post()
//                .bodyValue(data)
//                .retrieve()
//                .bodyToMono(String.class);
//
//
//        return responseMono.block();
//    }
}


