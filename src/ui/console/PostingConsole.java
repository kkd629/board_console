package javastory.club.stage3.step4.ui.console;

import java.util.List;
import java.util.Optional;

import javastory.club.stage3.step4.logic.ServiceLogicLycler;
import javastory.club.stage3.step4.service.BoardService;
import javastory.club.stage3.step4.service.PostingService;
import javastory.club.stage3.step4.service.ServiceLycler;
import javastory.club.stage3.step4.service.dto.BoardDto;
import javastory.club.stage3.step4.service.dto.PostingDto;
import javastory.club.stage3.step4.util.NoSuchBoardException;
import javastory.club.stage3.step4.util.NoSuchClubException;
import javastory.club.stage3.step4.util.NoSuchMemberException;
import javastory.club.stage3.step4.util.NoSuchPostingException;
import javastory.club.stage3.util.ConsoleUtil;
import javastory.club.stage3.util.Narrator;
import javastory.club.stage3.util.TalkingAt;

public class PostingConsole {
	private BoardDto currentBoard;
	
	private BoardService boardService;
	private PostingService postingService;
	
	private ConsoleUtil consoleUtil;
	private Narrator narrator;
	
	public PostingConsole() {
		ServiceLycler serviceFactory = ServiceLogicLycler.shareInstance();
		this.boardService = serviceFactory.createBoardService();
		this.postingService = serviceFactory.createPostingService();
		
		this.narrator = new Narrator(this, TalkingAt.Left);
		this.consoleUtil = new ConsoleUtil(narrator);
	}
	public boolean hasCurrentBoard() {
		return Optional.ofNullable(currentBoard).isPresent();
	}
	public String requestCurrentBoardName(){
		String clubName = null;
		if(hasCurrentBoard()) {
			clubName = currentBoard.getName();
		}
		return clubName;
	}
	public void findBoard() {
		BoardDto boardFound = null;
		while(true) {
			String clubName = consoleUtil.getValueOf("name");
			if(clubName.equals("0")) {
				break;
			}
			try {
				boardFound = boardService.findByClubName(clubName);
				narrator.sayln("found" + boardFound);
				break;
			} catch(NoSuchClubException e) {
				narrator.sayln(e.getMessage());
			}
			boardFound = null;
		}
		this.currentBoard = boardFound;
	}
	public void register() {
		if(!hasCurrentBoard()) {
			narrator.sayln("no");
			return;
		}
		while(true) {
			String title = consoleUtil.getValueOf("title");
			if(title.equals("0")) {
				return;
			}
			String writerEmail = consoleUtil.getValueOf("writerEamil");
			String contents = consoleUtil.getValueOf("contents");
			
			try {
				PostingDto postingDto = new PostingDto(title, writerEmail, contents);
				String postingId = postingService.register(currentBoard.getId(), postingDto);
				postingDto.setUsid(postingId);
				
				narrator.sayln("register" + postingDto);
			} catch(NoSuchBoardException | NoSuchMemberException e) {
				narrator.sayln(e.getMessage());
			}
		}
	}
	public void findByBoardId() {
		if(!hasCurrentBoard()) {
			narrator.sayln("no");
			return;
		}
		try {
			List<PostingDto> postings = postingService.findByBoardId(currentBoard.getId());
			int index = 0;
			for(PostingDto postingDto : postings) {
				narrator.sayln(String.format("[%d] ", index) + postingDto);
				index++;
			}
		}catch(NoSuchBoardException e) {
			narrator.sayln(e.getMessage());
		}
	}
	public void find() {
		if(!hasCurrentBoard()) {
			narrator.sayln("no");
			return;
		}
		PostingDto postingDto = null;
		while(true) {
			String postingId = consoleUtil.getValueOf("find");
			if(postingId.equals("0")) {
				break;
			}
			try {
				postingDto = postingService.find(postingId);
				narrator.sayln("found" + postingDto);
			} catch(NoSuchPostingException e) {
				narrator.sayln(e.getMessage());
			}
		}
	}
	public PostingDto findOne() {
		if(!hasCurrentBoard()) {
			narrator.sayln("no");
			return null;
		}
		PostingDto postingDto = null;
		while(true) {
			String postingId = consoleUtil.getValueOf("find");
			if(postingId.equals("0")) {
				break;
			}
			try {
				postingDto = postingService.find(postingId);
				narrator.sayln("found" + postingDto);
				break;
			}catch(NoSuchPostingException e) {
				narrator.sayln(e.getMessage());
			}
			postingDto = null;
		}
		return postingDto;
	}
	public void modify() {
		PostingDto targetPosting = findOne();
		if(targetPosting == null) {
			return;
		}
		String newTitle = consoleUtil.getValueOf("title");
		if(newTitle.equals("0")) {
			return;
		}
		targetPosting.setTitle(newTitle);
		
		String contents = consoleUtil.getValueOf("new");
		targetPosting.setContents(contents);
		
		try {
			postingService.modify(targetPosting);
			narrator.sayln("moodified" + targetPosting);
		} catch(NoSuchPostingException e) {
			narrator.sayln(e.getMessage());
		}
	}
	public void remove() {
		PostingDto targetPosting = findOne();
		if(targetPosting == null) {
			return;
		}
		String confirmStr = consoleUtil.getValueOf("remove");
		if(confirmStr.toLowerCase().equals("y") || confirmStr.toLowerCase().equals("yes")) {
			narrator.sayln("removing" + targetPosting.getTitle());
			postingService.remove(targetPosting.getUsid());
		}else {
			narrator.sayln("cancel" + targetPosting.getTitle());
		}
	}
}


































