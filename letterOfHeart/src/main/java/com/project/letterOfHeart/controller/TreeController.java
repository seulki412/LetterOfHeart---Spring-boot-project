package com.project.letterOfHeart.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.letterOfHeart.domain.Tree;
import com.project.letterOfHeart.service.TreeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TreeController {

	private final TreeService treeService;

	@GetMapping("/{id}/edit")
	public String designTree(Model model , @PathVariable("id")Long id) {
		Tree tree = new Tree();
		model.addAttribute("tree",tree);
		model.addAttribute("id",id);
		
		return "designTree";
	}
	
	@PostMapping("{id}/id/designTree")
	public String editTree(Model model , HttpServletRequest request) {
		Long id = (long) Integer.parseInt(request.getParameter("id")) ;
		int imgNum = Integer.parseInt( request.getParameter("imgNum") ) ;
		treeService.editDesign( id,imgNum ) ;
		return "redirect:/myTrees/{id}" ;
	}
}
