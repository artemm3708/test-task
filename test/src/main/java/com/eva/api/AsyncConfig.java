package com.eva.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class AsyncConfig {

  @Bean
  public WebMvcConfigurer webMvcConfigurer(ConcurrentTaskExecutor concurrentTaskExecutor) {
    return new WebMvcConfigurer() {
      @Override
      public void configureAsyncSupport(@NonNull AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(-1);
        configurer.setTaskExecutor(concurrentTaskExecutor);
      }
    };
  }
  @Bean
  public ConcurrentTaskExecutor concurrentTaskExecutor() {
    return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(5, new ThreadFactory() {
      private final AtomicInteger threadCounter = new AtomicInteger(0);
      @Override
      public Thread newThread(@NonNull Runnable runnable) {
        return new Thread(runnable, "asyncThread-" + threadCounter.incrementAndGet());
      }
    }));
  }

}