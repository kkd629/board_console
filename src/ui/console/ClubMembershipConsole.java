package javastory.club.stage3.step4.ui.console;

import javastory.club.stage3.step1.entity.club.RoleInClub;
import javastory.club.stage3.step4.logic.ServiceLogicLycler;
import javastory.club.stage3.step4.service.ClubService;
import javastory.club.stage3.step4.service.ServiceLycler;
import javastory.club.stage3.step4.service.dto.ClubMembershipDto;
import javastory.club.stage3.step4.service.dto.TravelClubDto;
import javastory.club.stage3.step4.util.MemberDuplicationException;
import javastory.club.stage3.step4.util.NoSuchClubException;
import javastory.club.stage3.step4.util.NoSuchMemberException;
import javastory.club.stage3.util.ConsoleUtil;
import javastory.club.stage3.util.Narrator;
import javastory.club.stage3.util.StringUtil;
import javastory.club.stage3.util.TalkingAt;

import java.util.Optional;

public class ClubMembershipConsole {
	private TravelClubDto currentClub;
	
	private ClubService clubService;
	
	private ConsoleUtil consoleUtil;
	private Narrator narrator;
	
	public ClubMembershipConsole() {
		ServiceLycler serviceFactory = ServiceLogicLycler.shareInstance();
		this.clubService = serviceFactory.createClubService();
		
		this.narrator = new Narrator(this, TalkingAt.Left);
		this.consoleUtil = new ConsoleUtil(narrator);
	}
	public boolean hasCurrentClub() {
		return Optional.ofNullable(currentClub).isPresent();
	}
	public String requestCurrentClubName() {
		String clubName = null;
		
		if(hasCurrentClub()) {
			clubName = currentClub.getName();
		}
		return clubName;
	}
	public void findClub() {
		TravelClubDto clubFound = null;
		while(true) {
			String clubName = consoleUtil.getValueOf("name");
			if(clubName.equals("0")) {
				break;
			}
			try {
				clubFound = clubService.findClubByName(clubName);
				narrator.sayln("found" + clubFound);
				break;
			} catch(NoSuchClubException e) {
				narrator.say(e.getMessage());
			}
			clubFound = null;
		}
		this.currentClub = clubFound;
	}
	public void add() {
		if(!hasCurrentClub()) {
			narrator.sayln("no");
			return;
		}
		while(true) {
			String email = consoleUtil.getValueOf("email");
			if(email.equals("0")) {
				return;
			}
			if(currentClub.getMembershipList().isEmpty()) {
				
			}
			String memberRole = consoleUtil.getValueOf("p|m");
			
			try {
				ClubMembershipDto clubMembershipDto = new ClubMembershipDto(currentClub.getUsid(), email);
				clubMembershipDto.setRole(RoleInClub.valueOf(memberRole));
				
				clubService.addMembership(clubMembershipDto);
				narrator.sayln(String.format("add", email, currentClub.getName()));
			} catch(MemberDuplicationException | NoSuchClubException e) {
				narrator.sayln(e.getMessage());
			} catch(IllegalArgumentException e) {
				narrator.sayln("p or m");
			}
		}
	}
	public void find() {
		if(!hasCurrentClub()) {
			narrator.sayln("no");
			return;
		}
		ClubMembershipDto membershipDto = null;
		while(true) {
			String memberEmail = consoleUtil.getValueOf("email");
			if(memberEmail.equals("0")) {
				break;
			}
			try {
				membershipDto = clubService.findMembershipIn(currentClub.getUsid(), memberEmail);
				narrator.sayln("membership" + membershipDto);
			} catch(NoSuchMemberException e){
				narrator.sayln(e.getMessage());
			}
		}
	}
	public ClubMembershipDto findOne() {
		ClubMembershipDto membershipDto = null;
		while(true) {
			String memberEmail = consoleUtil.getValueOf("email");
			if(memberEmail.equals("0")) {
				break;
			}
			try {
				membershipDto = clubService.findMembershipIn(currentClub.getUsid(), memberEmail);
				narrator.sayln("found" + membershipDto);
				break;
			} catch(NoSuchMemberException e) {
				narrator.sayln(e.getMessage());
			}
		}
		return membershipDto;
	}
	public void modify() {
		if(!hasCurrentClub()) {
			narrator.sayln("no");
			return;
		}
		ClubMembershipDto targetMembership = findOne();
		if(targetMembership == null) {
			return;
		}
		String newRole = consoleUtil.getValueOf("new p|m");
		if(newRole.equals("0")) {
			return;
		}
		if(!StringUtil.isEmpty(newRole)) {
			targetMembership.setRole(RoleInClub.valueOf(newRole));
		}
		String clubId = targetMembership.getClubId();
		clubService.modifyMembership(clubId, targetMembership);
		
		ClubMembershipDto modifiedMembership = clubService.findMembershipIn(clubId, targetMembership.getMemberEmail());
		narrator.sayln("modified" + modifiedMembership);
	}
	public void remove() {
		if(!hasCurrentClub()) {
			narrator.sayln("no");
			return;
		}
		ClubMembershipDto targetMembership = findOne();
		if(targetMembership == null) {
			return;
		}
		String confirmStr = consoleUtil.getValueOf("remove");
		if(confirmStr.toLowerCase().equals("y") || confirmStr.toLowerCase().equals("yes")) {
			narrator.sayln("removing" + targetMembership.getMemberEmail());
			clubService.removeMembership(currentClub.getUsid(), targetMembership.getMemberEmail());
		} else {
			narrator.sayln("cancel" + targetMembership.getMemberEmail());
		}
	}
}




























