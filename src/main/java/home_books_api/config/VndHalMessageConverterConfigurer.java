package home_books_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;

@Configuration
public class VndHalMessageConverterConfigurer {

	private static final List<MediaType> SUPPORTED_MEDIA_TYPES = List
			.of(v(1), HAL_JSON);

	@Autowired
	public void configureVndHalMessageConverter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
		findHalMessageConverter(requestMappingHandlerAdapter).ifPresent(
			httpMessageConverter -> httpMessageConverter
				.setSupportedMediaTypes(SUPPORTED_MEDIA_TYPES));
	}

	private Optional<MappingJackson2HttpMessageConverter> findHalMessageConverter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
		return requestMappingHandlerAdapter.getMessageConverters()
			.stream()
			.filter(httpMessageConverter -> httpMessageConverter instanceof MappingJackson2HttpMessageConverter && httpMessageConverter
				.getSupportedMediaTypes().contains(HAL_JSON))
			.map(MappingJackson2HttpMessageConverter.class::cast)
			.findAny();
	}

	private static MediaType v(int versionNumber) {
		return new MediaType("application",
				"vnd.homebooks.v" + versionNumber + "+hal+json");
	}
}
