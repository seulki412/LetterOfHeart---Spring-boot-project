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

		/* post????????? ????????? user ??????????????? Validation??? ????????? ?????? */
		if (errors.hasErrors()) {
			model.addAttribute("LoginForm", form);
			Map<String, String> validateResult = usersService.validateHandler(errors);
			for (String key : validateResult.keySet()) {
				model.addAttribute(key, validateResult.get(key));
			}
			model.addAttribute("loginMsg", "????????? ??????!!!!!!!!");
			return new ModelAndView("/index");
		}

		Users loginUsers = usersService.login(form.getAccountId(), form.getPassword());
		if (loginUsers == null) {
			// ????????? ??????
			model.addAttribute("loginMsg", "????????? ??????!!!!!!!!");
			return new ModelAndView("/index");
		}

//		redirectAttributes.addAttribute("id", loginUsers.getId());

		loginToken(form, response);

		String refreshToken = loginToken(form, response).getRefreshToken();
		String accessToken = loginToken(form, response).getAccessToken();

		// ?????? ????????? ??????
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
		
		/*?????? ????????? 3??? */
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
		// ?????? ??????
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
		// ?????? ??????
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

		// urlrnd????????? user??????????????? id?????? ???????????? ?

		ModelAndView mv = new ModelAndView("myTree");
		System.out.println("????????? ?????? ? : " + urlrnd);
		Long id = usersService.findUserIdUserUrlrnd(urlrnd);

		Users loginUsers = new Users();
		// ????????? ??? ??????????????? ??????
		Tree tree = treeService.findOne(id);
		loginUsers.addTree(tree);

		model.addAttribute("userForm", new UsersForm());
		model.addAttribute("id", id);
		// ?????? id??? ????????? ?????????
		model.addAttribute("messages", messageService.messageList(id));
		// ????????? ??????
		model.addAttribute("msgCnt", messageService.findAllById(id));
		// ????????? ??????
		model.addAttribute("pages", pageService.pageList(pageable, id));
		System.out.println("page : " + pageService.pageList(pageable, id));
		model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
		System.out.println("previous : " + pageable.previousOrFirst().getPageNumber());
		model.addAttribute("next", pageable.next().getPageNumber());
		System.out.println("next : " + pageable.next().getPageNumber());

		// ???????????????
		model.addAttribute("treeDesign", tree.getTreeDesign());
		System.out.println("??????????????? : " + tree.getTreeDesign());

		return mv;
	}

	@GetMapping("/myTrees/{id}")
	public ModelAndView getId(Model model, @PathVariable("id") long id,
	//public ModelAndView getId(Model model, @PathVariable("urlrnd") String urlrnd,
			@PageableDefault(sort = "id", direction = Direction.ASC, size = 6) Pageable pageable,
			@CookieValue("accessToken") String accessToken, @CookieValue("refreshToken") String refreshToken) {

		// urlrnd????????? user??????????????? id?????? ???????????? ?

		ModelAndView mv = new ModelAndView("myTree");
		System.out.println("????????? ?????? ? : " + id);
		//Long id = usersService.findUserIdUserUrlrnd(id);
		Users loginUsers = new Users();
		// ????????? ??? ??????????????? ??????
		Tree tree = treeService.findOne(id);
		loginUsers.addTree(tree);

		model.addAttribute("userForm", new UsersForm());
		model.addAttribute("id", id);
		// ?????? id??? ????????? ?????????
		model.addAttribute("messages", messageService.messageList(id));
		// ????????? ??????
		model.addAttribute("msgCnt", messageService.findAllById(id));
		// ????????? ??????
		model.addAttribute("pages", pageService.pageList(pageable, id));
		System.out.println("page : " + pageService.pageList(pageable, id));
		model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
		System.out.println("previous : " + pageable.previousOrFirst().getPageNumber());
		model.addAttribute("next", pageable.next().getPageNumber());
		System.out.println("next : " + pageable.next().getPageNumber());

		// ???????????????
		model.addAttribute("treeDesign", tree.getTreeDesign());
		System.out.println("??????????????? : " + tree.getTreeDesign());

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

	// ????????????
	@PostMapping("/users/new")
	public ModelAndView create(LoginForm loginForm, @Valid UsersForm form, Errors errors, Model model) {

		ModelAndView mv = new ModelAndView("redirect:/");
		// ?????? ??????
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 25;
		Random random = new Random();
		// ?????? ???
		String urlrnd = random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		if (errors.hasErrors()) {
			/* ???????????? ????????? ?????? ????????? ?????? */
			model.addAttribute("usersForm", form);
			/* ???????????? ????????? message ????????? ????????? ???????????? View??? ?????? */
			Map<String, String> validateResult = usersService.validateHandler(errors);
			// map.keySet() -> ?????? key?????? ????????????.
			// ??? ????????? ?????? ???????????? ?????? ?????? ?????? ???????????? ??????
			for (String key : validateResult.keySet()) {
				// ex) model.addAtrribute("valid_id", "???????????? ?????? ???????????? ?????????.")
				model.addAttribute(key, validateResult.get(key));
			}
			model.addAttribute("msg", "???????????? ??????!!!!!!!!");
			model.addAttribute("success", "400");

			return new ModelAndView("/index");
		}

		// ???????????? ????????? ?????? ??????
		Users joinUsers = usersService.join(form.getAccountId());
		if (joinUsers != null) {
			// ??????
			model.addAttribute("msgg", "????????? ??????, ???????????? ??????!!");
			model.addAttribute("success", "400");
			return new ModelAndView("/index");
		}

//		redirectAttributes.addAttribute("id", joinUsers.getId());

		// ?????? ??????, service
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

		// ????????????
		usersService.join(users);
		// ?????? ??????
		treeService.save(tree);

		return mv;
	}

}
