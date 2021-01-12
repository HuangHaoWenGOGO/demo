package seassoon.rule.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: 张前峰
 * @Date: 2019/6/27 15:07
 * @Description:
 */
@Component
public class CorsFilter implements Filter {

	@Override
	public void init(FilterConfig fc) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
	                     FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;


		response.addHeader("Access-Control-Allow-Credentials", "true");
		String headerOrigin = "Origin";
		if (StringUtils.isNotEmpty(request.getHeader(headerOrigin))) {
			response.addHeader("Access-Control-Allow-Origin", request.getHeader(headerOrigin));
		}

		response.addHeader("Access-Control-Allow-Headers",
				"Authentication,Authorization,Origin, X-Requested-With, Content-Type, Accept,X-Token");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT,DELETE,OPTIONS,PATCH");
		response.addHeader("Access-Control-Expose-Headers",
				"Authorization,Set-Cookie");

		String method = request.getMethod();
		String methodType = "OPTIONS";
		if(null != method && method.toUpperCase().equals(methodType)) {
			response.setStatus(HttpStatus.NO_CONTENT.value());

		}else{
			chain.doFilter(req, resp);
		}






	}

	@Override
	public void destroy() {
	}

}
