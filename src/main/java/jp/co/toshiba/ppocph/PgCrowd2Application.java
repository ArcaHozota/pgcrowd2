package jp.co.toshiba.ppocph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import lombok.extern.log4j.Log4j2;

/**
 * Ogumaアプリケーション
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Log4j2
@SpringBootApplication
@ServletComponentScan
public class PgCrowd2Application {
	public static void main(final String[] args) {
		SpringApplication.run(PgCrowd2Application.class, args);
		log.info(PgCrowdConstants.MESSAGE_SPRING_APPLICATION);
	}
}
