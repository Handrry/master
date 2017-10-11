package com.master.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin // 解决跨域请求
@Controller
@RequestMapping("/api")
public class HomeController {
	// 验证码字符集
	private static final char[] chars = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	// 字符数量
	private static final int SIZE = 4;
	// 干扰线数量
	private static final int LINES = 5;
	// 宽度
	private static final int WIDTH = 80;
	// 高度
	private static final int HEIGHT = 40;
	// 字体大小
	private static final int FONT_SIZE = 25;


	@RequestMapping("/createImg.do")
	protected void createImg(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 生产随机验证码和图片
		String code = getAuthCode();
		BufferedImage image = getAuthImage(code);

		HttpSession session = req.getSession();
		session.setAttribute("img", code);
		System.out.println("AuthImageServlet======================>"+code);
		System.out.println("sessionId======================>"+session.getId());
		// 设置图片类型输出
		res.setContentType("image/png");
		OutputStream os = res.getOutputStream();
		ImageIO.write(image, "png", os);
		os.close();

	}

	@RequestMapping("/test")
	@ResponseBody
	public Map<String,Object> testURL(@RequestParam("sign")String sign){
		System.out.println("get sign ======================>" + sign);
		String income = "g62mOV5omSYlj+g/d1sMsasTjBJfA9ocArWvhCohjI6/AT/MKCs5zSybVqNdIxuZRuoEeFmFw135R+RUki/vqU77L5uz5VgfQT7IiKs2UNsg7rzyLDYzSLbpIes6Ihu3GT/TT0Exykoo9CN+TzrvHaCrVAsDMB2XsKmrVIcUgKE=";
		Map<String,Object> r = new HashMap<>();
		r.put("incom", income);
		r.put("s",true);
		r.put("f",false);
		return r;
	}


	/**
	 * 随机取色
	 */
	private static Color getRandomColor() {
		Random ran = new Random();
		Color color = new Color(ran.nextInt(256),
				ran.nextInt(256), ran.nextInt(256));
		return color;
	}

	private BufferedImage getAuthImage(String authCode) {
		// 1.创建空白图片
		BufferedImage image = new BufferedImage(
				WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		// 2.获取图片画笔
		Graphics graphic = image.getGraphics();
		// 3.设置画笔颜色
		graphic.setColor(Color.LIGHT_GRAY);
		// 4.绘制矩形背景
		graphic.fillRect(0, 0, WIDTH, HEIGHT);
		// 5.画随机字符
		Random ran = new Random();
		String[]  authCodes= authCode.split("");
		for (int i = 0; i <authCodes.length; i++) {
			// 设置随机颜色
			graphic.setColor(getRandomColor());
			// 设置字体大小
			graphic.setFont(new Font(
					null, Font.BOLD + Font.ITALIC, FONT_SIZE));
			// 画字符
			graphic.drawString(
					authCodes[i] + "", i * WIDTH / SIZE, (int)(HEIGHT * 0.8));
			// 记录字符
		}
		// 6.画干扰线
		for (int i = 0; i < LINES; i++) {
			// 设置随机颜色
			graphic.setColor(getRandomColor());
			// 随机画线
			graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT),
					ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
		}
		// 7.返回验证码和图片
		return image;
	}

	private String getAuthCode() {
		StringBuffer sb = new StringBuffer();
		Random ran = new Random();
		for (int i = 0; i <SIZE; i++) {
			int n = ran.nextInt(chars.length);
			sb.append(chars[n]);
		}
		return sb.toString();
	}

	@RequestMapping("/chat.do")
	@ResponseBody
	public Map<String,Object> chat(HttpServletResponse res){
		//setHttpHeader(res);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> chat = new HashMap<String, Object>();
		chat.put("zhangsan:", "大家好");
		chat.put("lisi:", "初次见面");
		chat.put("wangwu:", "你好");
		result.put("code", 200);
		result.put("msg", chat);
		return result;
	}

	@RequestMapping(value ="/login")
	@ResponseBody
	public R login(HttpServletRequest req){
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String code = req.getParameter("code");

		HttpSession session = req.getSession();
		String img = (String) session.getAttribute("img");
		System.out.println(username+":"+password+":"+code);
		System.out.println(img);
		if(!img.toUpperCase().equals(code)){
			return new R(null,R.error,"验证码错误");
		}
		if(username!="admin"||password!="123456"){
			return new R(null,R.error,"用户名密码不正确");
		}
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("token", "654321");
		return new R(data,R.success,null);
	}


	// 设置请求头
	public void setHttpHeader(HttpServletResponse res){
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		res.setHeader("Access-Control-Max-Age", "0");
		res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("XDomainRequestAllowed","1");
	}
}
