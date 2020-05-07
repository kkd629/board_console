package javastory.club.stage3.step4.ui.console;


import javastory.club.stage3.step1.util.InvalidEmailException;
import javastory.club.stage3.step4.logic.ServiceLogicLycler;
import javastory.club.stage3.step4.service.MemberService;
import javastory.club.stage3.step4.service.dto.MemberDto;
import javastory.club.stage3.step4.util.MemberDuplicationException;
import javastory.club.stage3.step4.util.NoSuchMemberException;
import javastory.club.stage3.util.ConsoleUtil;
import javastory.club.stage3.util.Narrator;
import javastory.club.stage3.util.TalkingAt;

public class MemberConsole {
	private MemberService memberService;
	
	private ConsoleUtil consoleUtil;
	private Narrator narrator;
	
	public MemberConsole() {
		this.memberService = ServiceLogicLycler.shareInstance().createMemberService();
		this.narrator = new Narrator(this, TalkingAt.Left);
		this.consoleUtil = new ConsoleUtil(narrator);
	}
	public void register() {
		while(true) {
			String email = consoleUtil.getValueOf("email");
			if(email.equals("0")) {
				return;
			}
			String name = consoleUtil.getValueOf("name");
			String phoneNumber = consoleUtil.getValueOf("phone");
			String nickName = consoleUtil.getValueOf("nickname");
			String birthDay = consoleUtil.getValueOf("birth");
			
			try {
				MemberDto newMember = new MemberDto(email, name, phoneNumber);
				newMember.setNickName(nickName);
				newMember.setBirthDay(birthDay);
				
				memberService.register(newMember);
				narrator.sayln("registered" + newMember.toString());
			} catch(MemberDuplicationException | InvalidEmailException e) {
				narrator.sayln(e.getMessage());
			}
		}
	}
	public void find() {
		while(true) {
			String email = consoleUtil.getValueOf("email");
			if(email.equals("0")) {
				return;
			}
			try {
				MemberDto memberFound = memberService.find(email);
				narrator.sayln("found" + memberFound.toString());
			}catch(NoSuchMemberException e) {
				narrator.sayln(e.getMessage());
			}
		}
	}
	public MemberDto findOne() {
		MemberDto memberFound = null;
		while(true) {
			String email = consoleUtil.getValueOf("email");
			if(email.equals("0")) {
				return null;
			}
			try {
				memberFound = memberService.find(email);
				narrator.sayln("found" + memberFound.toString());
				break;
			} catch(NoSuchMemberException e) {
				narrator.sayln(e.getMessage());
			}
			memberFound = null;
		}
		return memberFound;
	}
	public void findByName() {
		while(true) {
			String name = consoleUtil.getValueOf("name");
			if(name.equals("0")) {
				return;
			}
			try {
				narrator.sayln("found memeber list");
				memberService.findByName(name).stream().forEach(member->narrator.sayln(member.toString()));
			}catch(NoSuchMemberException e) {
				narrator.sayln(e.getMessage());
			}
		}
	}
	public void modify() {
		MemberDto targetMember = findOne();
		if(targetMember == null) {
			return;
		}
		String newName = consoleUtil.getValueOf("new name");
		targetMember.setName(newName);
		String newPhoneNumber = consoleUtil.getValueOf("new phone");
		targetMember.setPhoneNumber(newPhoneNumber);
		String newNickName = consoleUtil.getValueOf("new nick");
		targetMember.setNickName(newNickName);
		String newBirthDay = consoleUtil.getValueOf("new birth");
		targetMember.setBirthDay(newBirthDay);
		
		try {
			memberService.modify(targetMember);
			narrator.sayln("modified" + targetMember.toString());
		} catch(NoSuchMemberException | InvalidEmailException e) {
			narrator.sayln(e.getMessage());
		}
	}
	public void remove() {
		MemberDto targetMember = findOne();
		if(targetMember == null) {
			return;
		}
		String confirmStr = consoleUtil.getValueOf("remove");
		if(confirmStr.toLowerCase().equals("y") || confirmStr.toLowerCase().equals("yes")) {
			narrator.sayln("removing" + targetMember.getName());
			memberService.remove(targetMember.getEmail());
		}else {
			narrator.sayln("cancel" + targetMember.getName());
		}
	}
}




























