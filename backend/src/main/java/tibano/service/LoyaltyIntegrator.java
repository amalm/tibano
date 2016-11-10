package tibano.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Future;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
@Service
public class LoyaltyIntegrator {
	private final static Logger LOGGER = LoggerFactory.getLogger(LoyaltyIntegrator.class);

	@Async
	public Future<Boolean> addPoints(Integer points) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://loyaltydemo.skidataus.com/DesktopModules/v1/API/points");
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("malmborg", "hendr!x");
		AsyncResult<Boolean> falseResult = new AsyncResult<Boolean>(Boolean.FALSE);
		try {
			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
		} catch (AuthenticationException e) {
			LOGGER.error("Could not add authentication header.", e);
			return falseResult;
		}
		StringBuilder json = new StringBuilder(
				"{\"portalId\":55,\"userId\":1030736,\"activityId\":1795,\"pointsAwarded\":");
		json.append(points.toString())
				.append(",\"awardedBy\":\"daves\",\"contentType\":\"parking.logic ID\",\"contentId\":55555}");
		StringEntity entity;
		try {
			entity = new StringEntity(json.toString());
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Loyalty points encoding error", e);
			return falseResult;
		}
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		try {
			CloseableHttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				LOGGER.info("Added {} loyalty points", points);
				return new AsyncResult<Boolean>(Boolean.TRUE);
			} else {
				LOGGER.error("Failed to add {} loyalty points:{}/{}", points, response.getStatusLine().getStatusCode(),
						response.getStatusLine().getReasonPhrase());
			}
		} catch (IOException e) {
			LOGGER.error("Failed to add {} loyalty points", points, e);
		}
		return falseResult;

	}

}
