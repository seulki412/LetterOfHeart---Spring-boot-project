package com.project.letterOfHeart.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.letterOfHeart.domain.Tree;
import com.project.letterOfHeart.domain.Users;
import com.project.letterOfHeart.dto.LoginForm;
import com.project.letterOfHeart.dto.MessageForm;
import com.project.letterOfHeart.dto.UsersForm;
import com.project.letterOfHeart.jwt.JwtAuthenticationFilter;
import com.project.letterOfHeart.jwt.TokenInfo;
import com.project.letterOfHeart.service.MessageService;
import com.project.letterOfHeart.service.PageService;
import com.project.letterOfHeart.service.TreeService;
import com.project.letterOfHeart.service.UsersService;
import com.project.letterOfHeart.service.UsersTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
public class UsersController {

	private final UsersService usersService;
	private final UsersTokenService usersTokenService;
	private final TreeService treeService;
	private final PageService pageService;
	private final MessageService messageService;
	private ResponseCookie cookie;
	private final JwtAuthenticationFilter filter;

	@GetMapping("/")
	public ModelAndView loginForm1(@ModelAttribute("loginForm") LoginForm loginForm, Model model,
			@ModelAttribute("usersForm") UsersForm usersForm) {
		ModelAndView mv = new ModelAndView("index");
		// model.addAttribute("loginForm", loginForm);

		return mv;
	}

	/*
	 * @GetMapping("/users/login") public ModelAndView loginForm(LoginForm
	 * loginForm, Model model) { ModelAndView mv = new ModelAndView("myTree");
	 * model.addAttribute("loginForm", loginForm); return mv; }
	 */

	@PostMapping("/users/login")
	public ModelAndView login(UsersForm usersForm, @Valid LoginForm form, Errors errors, Model model,
			RedirectAttributes redirectAttributes, HttpServletResponse response, MessageForm messageForm) {

		// ModelAndView mv = new ModelAndView("redirect:/myTree/{id}");

		/* post요청시 넘어온 user 입력값에서 Validation에 걸리는 경우 */
		if (errors.hasErrors()) {
			model.addAttribute("LoginForm", form);
			Map<String, String> validateResult = usersService.validateHandler(errors);
			for (String key : validateResult.keySet()) {
				model.addAttribute(key, validateResult.get(key));
			}
			model.addAttribute("loginMsg", "로그인 실패!!!!!!!!");
			return new ModelAndView("/index");
		}

		Users loginUsers = usersService.login(form.getAccountId(), form.getPassword());
		if (loginUsers == null) {
			// 로그인 실패
			model.addAttribute("loginMsg", "로그인 실패!!!!!!!!");
			return new ModelAndView("/index");
		}

//		redirectAttributes.addAttribute("id", loginUsers.getId());

		loginToken(form, response);

		String refreshToken = loginToken(form, response).getRefreshToken();
		String accessToken = loginToken(form, response).getAccessToken();

		// 토큰 쿠키에 저장
		Cookie accessCookie = new Cookie("accessToken", accessToken);
		accessCookie.setPath("/");
		accessCookie.setHttpOnly(true);
		accessCookie.setSecure(true);
		accessCookie.setMaxAge(15 * 60 * 60 * 60);
		response.addCookie(accessCookie);

		Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
		refreshCookie.setPath("/");
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(true);
		refreshCookie.setMaxAge(24 * 60 * 60 * 60);
		response.addCookie(refreshCookie);
		
		/*새로 추가된 3줄 */
		Tree tree = treeService.findOne( loginUsers.getId() ) ;
		loginUsers.addTree(tree) ;
		
		model.addAttribute("treeDesign", tree.getTreeDesign());
		
		return new ModelAndView("redirect:/myTree/" + loginUsers.getUrlrnd());
	}

	public TokenInfo loginToken(@RequestBody LoginForm loginForm, HttpServletResponse response) {

		String accoutid = loginForm.getAccountId();
		String password = loginForm.getPassword();

		Users loginUsers = usersService.login(loginForm.getAccountId(), loginForm.getPassword());

		TokenInfo tokenInfo = usersTokenService.login(accoutid, password);

		return tokenInfo;
	}

	@GetMapping("/users/logout")
	public ModelAndView logout(HttpServletRequest request ,HttpServletResponse response) {
		// 쿠키 삭제
		Cookie accessCookie = new Cookie("accessToken", null);
		accessCookie.setHttpOnly(true);
		accessCookie.setSecure(false);
		accessCookie.setMaxAge(0);
		accessCookie.setPath("/");
		response.addCookie(accessCookie);

		Cookie refreshCookie = new Cookie("refreshToken", null);
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(false);
		refreshCookie.setMaxAge(0);
		refreshCookie.setPath("/");
		response.addCookie(refreshCookie);
		
		Cookie jssesionCookie = new Cookie("JSESSIONID", null);
		jssesionCookie.setHttpOnly(true);
		jssesionCookie.setSecure(false);
		jssesionCookie.setMaxAge(0);
		jssesionCookie.setPath("/");
		response.addCookie(jssesionCookie);
		
		Cookie[] ckList = request.getCookies();
		
		for ( Cookie ck : ckList )
			ck.setMaxAge(0) ;


		ModelAndView mav = new ModelAndView("redirect:/");
		return mav;
	}

	@PostMapping("/users/logout")
	public ModelAndView logoutPost(HttpServletRequest request , HttpServletResponse response) {
		// 쿠키 삭제
		Cookie accessCookie = new Cookie("accessToken", null);
		accessCookie.setHttpOnly(true);
		accessCookie.setSecure(false);
		accessCookie.setMaxAge(0);
		accessCookie.setPath("/");
		response.addCookie(accessCookie);

		Cookie refreshCookie = new Cookie("refreshToken", null);
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(false);
		refreshCookie.setMaxAge(0);
		refreshCookie.setPath("/");
		response.addCookie(refreshCookie);
		
		Cookie jssesionCookie = new Cookie("JSESSIONID", null);
		jssesionCookie.setHttpOnly(true);
		jssesionCookie.setSecure(false);
		jssesionCookie.setMaxAge(0);
		jssesionCookie.setPath("/");
		response.addCookie(jssesionCookie);

		ModelAndView mav = new ModelAndView("redirect:/");
		return mav;
	}

	@GetMapping("/myTree/{urlrnd}")
	// public ModelAndView getId(Model model, @PathVariable("id") long id,
	public ModelAndView getId(Model model, @PathVariable("urlrnd") String urlrnd,
			@PageableDefault(sort = "id", direction = Direction.ASC, size = 6) Pageable pageable,
			@CookieValue("accessToken") String accessToken, @CookieValue("refreshToken") String refreshToken) {

		// urlrnd가지고 user테이블에서 id값을 뽑아내죠 ?

		ModelAndView mv = new ModelAndView("myTree");
		System.out.println("오는게 맞나 ? : " + urlrnd);
		Long id = usersService.findUserIdUserUrlrnd(urlrnd);

		Users loginUsers = new Users();
		// 로그인 후 트리이미지 반환
		Tree tree = treeService.findOne(id);
		loginUsers.addTree(tree);

		model.addAttribute("userForm", new UsersForm());
		model.addAttribute("id", id);
		// 해당 id의 메세지 리스트
		model.addAttribute("messages", messageService.messageList(id));
		// 메세지 개수
		model.addAttribute("msgCnt", messageService.findAllById(id));
		// 페이징 처리
		model.addAttribute("pages", pageService.pageList(pageable, id));
		System.out.println("page : " + pageService.pageList(pageable, id));
		model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
		System.out.println("previous : " + pageable.previousOrFirst().getPageNumber());
		model.addAttribute("next", pageable.next().getPageNumber());
		System.out.println("next : " + pageable.next().getPageNumber());

		// 디자인트리
		model.addAttribute("treeDesign", tree.getTreeDesign());
		System.out.println("트리디자인 : " + tree.getTreeDesign());

		return mv;
	}

	@GetMapping("/myTrees/{id}")
	public ModelAndView getId(Model model, @PathVariable("id") long id,
	//public ModelAndView getId(Model model, @PathVariable("urlrnd") String urlrnd,
			@PageableDefault(sort = "id", direction = Direction.ASC, size = 6) Pageable pageable,
			@CookieValue("accessToken") String accessToken, @CookieValue("refreshToken") String refreshToken) {

		// urlrnd가지고 user테이블에서 id값을 뽑아내죠 ?

		ModelAndView mv = new ModelAndView("myTree");
		System.out.println("오는게 맞나 ? : " + id);
		//Long id = usersService.findUserIdUserUrlrnd(id);
		Users loginUsers = new Users();
		// 로그인 후 트리이미지 반환
		Tree tree = treeService.findOne(id);
		loginUsers.addTree(tree);

		model.addAttribute("userForm", new UsersForm());
		model.addAttribute("id", id);
		// 해당 id의 메세지 리스트
		model.addAttribute("messages", messageService.messageList(id));
		// 메세지 개수
		model.addAttribute("msgCnt", messageService.findAllById(id));
		// 페이징 처리
		model.addAttribute("pages", pageService.pageList(pageable, id));
		System.out.println("page : " + pageService.pageList(pageable, id));
		model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
		System.out.println("previous : " + pageable.previousOrFirst().getPageNumber());
		model.addAttribute("next", pageable.next().getPageNumber());
		System.out.println("next : " + pageable.next().getPageNumber());

		// 디자인트리
		model.addAttribute("treeDesign", tree.getTreeDesign());
		System.out.println("트리디자인 : " + tree.getTreeDesign());

		return mv;
	}

	
	@GetMapping("/designTree")
	// public ModelAndView designTree(HttpServletRequest request,
	// HttpServletResponse response, FilterChain chain) {
	public ModelAndView designTree(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		ModelAndView mv = new ModelAndView("designTree");
		try {
			filter.doFilter(request, response, chain);
			System.out.println(" out? ");
		} catch (IllegalStateException e) {
			System.out.println(" ILLEGAL  " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOE : " + e);

			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			System.out.println("SERE" + e);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(" AllE : " + e);
			e.printStackTrace();
		}
		return mv;
	}

	// 회원가입
	@PostMapping("/users/new")
	public ModelAndView create(LoginForm loginForm, @Valid UsersForm form, Errors errors, Model model) {

		ModelAndView mv = new ModelAndView("redirect:/");
		// 난수 생성
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 25;
		Random random = new Random();
		// 난수 값
		String urlrnd = random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		if (errors.hasErrors()) {
			/* 회원가입 실패시 입력 데이터 유지 */
			model.addAttribute("usersForm", form);
			/* 회원가입 실패시 message 값들을 모델에 매핑해서 View로 전달 */
			Map<String, String> validateResult = usersService.validateHandler(errors);
			// map.keySet() -> 모든 key값을 갖고온다.
			// 그 갖고온 키로 반복문을 통해 키와 에러 메세지로 매핑
			for (String key : validateResult.keySet()) {
				// ex) model.addAtrribute("valid_id", "아이디는 필수 입력사항 입니다.")
				model.addAttribute(key, validateResult.get(key));
			}
			model.addAttribute("msg", "회원가입 실패!!!!!!!!");
			model.addAttribute("success", "400");

			return new ModelAndView("/index");
		}

		// 회원가입 아이디 중복 체크
		Users joinUsers = usersService.join(form.getAccountId());
		if (joinUsers != null) {
			// 실패
			model.addAttribute("msgg", "아이디 중복, 회원가입 실패!!");
			model.addAttribute("success", "400");
			return new ModelAndView("/index");
		}

//		redirectAttributes.addAttribute("id", joinUsers.getId());

		// 정상 로직, service
		Users users = new Users();
		users.setAccountId(form.getAccountId());
		users.setPassword(form.getPassword());
		users.setUrlrnd(urlrnd);
		users.setNickname(form.getNickname());
		users.setPhone(form.getPhone());
		users.setCreateDate(LocalDateTime.now());
		users.setRole("USER");

		// tree
		Tree tree = new Tree();
		users.addTree(tree);
		tree.setId(users.getId());
		tree.setMessageCnt(0);
		tree.setTreeDesign(1);

		// 회원가입
		usersService.join(users);
		// 트리 생성
		treeService.save(tree);

		return mv;
	}

}
