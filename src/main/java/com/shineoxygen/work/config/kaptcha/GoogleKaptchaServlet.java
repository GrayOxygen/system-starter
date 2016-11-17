package com.shineoxygen.work.config.kaptcha;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import com.google.code.kaptcha.servlet.KaptchaServlet;

/**
 * @author 王辉阳
 * @date 2016年11月8日 下午2:51:48
 * @Description google校验码支持：servelt配置生成校验码，图片访问servlet可得，后台自行校验一致性
 */
@WebServlet(name = "Kaptcha", urlPatterns = "/Kaptcha", initParams = { //
		@WebInitParam(name = "kaptcha.border", value = "no", description = "边框设置"), //
		@WebInitParam(name = "kaptcha.border.color", value = "red", description = "边框颜色"), //
		@WebInitParam(name = "kaptcha.border.thickness", value = "5", description = "边框宽度"), //
		@WebInitParam(name = "kaptcha.textproducer.char.length", value = "4", description = "生成结果的字符个数"), //
		@WebInitParam(name = "kaptcha.textproducer.font.names", value = "微软雅黑,宋体,楷体", description = "生成结果的字符字体"), //
		@WebInitParam(name = "kaptcha.textproducer.font.size", value = "40", description = "生成结果的字符大小"), //
		@WebInitParam(name = "kaptcha.textproducer.font.color", value = "blue", description = "生成结果的字符 颜色"), //
		@WebInitParam(name = "kaptcha.textproducer.char.space", value = "2", description = "生成结果的字符 字间距"), //
		@WebInitParam(name = "kaptcha.noise.impl", value = "com.google.code.kaptcha.impl.DefaultNoise", description = "干扰线 生成器"), //
		@WebInitParam(name = "kaptcha.noise.color", value = "black", description = "干扰线颜色"), //
		@WebInitParam(name = "kaptcha.image.width", value = "200", description = "验证码图片宽度"), //
		@WebInitParam(name = "kaptcha.image.height", value = "50", description = "验证码图片高度"), //
		@WebInitParam(name = "kaptcha.noise.impl", value = "com.google.code.kaptcha.impl.DefaultNoise", description = "干扰线 生成器"), //
		@WebInitParam(name = "kaptcha.noise.impl", value = "com.google.code.kaptcha.impl.DefaultNoise", description = "干扰线 生成器"),//
})
public class GoogleKaptchaServlet extends KaptchaServlet {

}
