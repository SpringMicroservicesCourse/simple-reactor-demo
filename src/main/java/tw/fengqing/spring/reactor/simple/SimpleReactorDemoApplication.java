package tw.fengqing.spring.reactor.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Simple Reactor Demo Application
 * 演示Project Reactor基本用法的Spring Boot應用程式
 * 
 * @author fengqing
 * @since 2025
 */
@SpringBootApplication
@Slf4j
public class SimpleReactorDemoApplication implements ApplicationRunner {

	/**
	 * 應用程式入口點
	 * 
	 * @param args 命令行參數
	 */
	public static void main(String[] args) {
		SpringApplication.run(SimpleReactorDemoApplication.class, args);
	}

	/**
	 * Spring Boot應用程式啟動後執行的方法
	 * 演示Flux的基本操作，包括map、doOnRequest、doOnComplete等
	 * 
	 * @param args ApplicationArguments 應用程式參數
	 * @throws Exception 可能拋出的異常
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 創建一個包含1-6數字的Flux流
		Flux.range(1, 6)
				.doOnRequest(n -> log.info("Request {} number", n)) // 注意順序造成的區別
//				.publishOn(Schedulers.elastic())
				.doOnComplete(() -> log.info("Publisher COMPLETE 1"))
				// map操作：將每個元素進行數學運算（可能產生異常）
				.map(i -> {
					log.info("Publish {}, {}", Thread.currentThread(), i);
					return 10 / (i - 3);  // 當i=3時會產生除零異常
//					return i;
				})
				.doOnComplete(() -> log.info("Publisher COMPLETE 2"))
//				.subscribeOn(Schedulers.single())
//				.onErrorResume(e -> {
//					log.error("Exception {}", e.toString());
//					return Mono.just(-1);
//				})
//				.onErrorReturn(-1)
				// 訂閱流，定義處理正常值、錯誤和完成的回調
				.subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
						e -> log.error("error {}", e.toString()),
						() -> log.info("Subscriber COMPLETE")//,
//						s -> s.request(4)
				);
		// 等待異步操作完成
		Thread.sleep(2000);
	}
}

