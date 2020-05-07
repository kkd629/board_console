package javastory.club.stage3.step4.ui.console;

import javastory.club.stage3.step4.logic.ServiceLogicLycler;
import javastory.club.stage3.step4.service.ClubService;
import javastory.club.stage3.step4.service.dto.TravelClubDto;
import javastory.club.stage3.step4.util.ClubDuplicationException;
import javastory.club.stage3.step4.util.NoSuchClubException;
import javastory.club.stage3.util.ConsoleUtil;
import javastory.club.stage3.util.Narrator;
import javastory.club.stage3.util.TalkingAt;

public class ClubConsole {
	private ClubService clubService;
	
	private ConsoleUtil consoleUtil;
	private Narrator narrator;
	
	public ClubConsole() {
		this.clubService = ServiceLogicLycler.shareInstance().createClubService();
		this.narrator = new Narrator(this, TalkingAt.Left);
		this.consoleUtil = new ConsoleUtil(narrator);
	}
	public void register() {
		while(true) {
			String clubName = consoleUtil.getValueOf("name");
			if(clubName.equals("0")) {
				return;
			}
			String intro = consoleUtil.getValueOf("intro");
			if(intro.equals("0")) {
				return;
			}
			try {
				TravelClubDto clubDto = new TravelClubDto(clubName, intro);
				clubService.registerClub(clubDto);
				narrator.say("registered" + clubDto.toString());
			}catch(IllegalArgumentException | ClubDuplicationException e) {
				narrator.sayln(e.getMessage());
			}
		}
	}
	public TravelClubDto find() {
		TravelClubDto clubFound = null;
		while(true) {
			String clubName = consoleUtil.getValueOf("name");
			if(clubName.equals("0")) {
				break;
			}
			try {
				clubFound = clubService.findClubByName(clubName);
				narrator.sayln("found" + clubFound);
			} catch(NoSuchClubException e) {
				narrator.sayln(e.getMessage());
			}
		}
		return clubFound;
	}
	public TravelClubDto findOne() {
		TravelClubDto clubFound = null;
		while(true) {
			String clubName = consoleUtil.getValueOf("find");
			if(clubName.equals("0")) {
				break;
			}
			try {
				clubFound = clubService.findClubByName(clubName);
				narrator.sayln("found" + clubFound);
				break;
			} catch (NoSuchClubException e) {
				narrator.sayln(e.getMessage());
			}
		}
		return clubFound;
	}
	public void modify() {
		TravelClubDto targetClub = findOne();
		if(targetClub == null) {
			return;
		}
		String newName = consoleUtil.getValueOf("name");
		if(newName.equals("0")) {
			return;
		}
		targetClub.setName(newName);
		
		String newIntro = consoleUtil.getValueOf("intro");
		targetClub.setIntro(newIntro);
		
		try {
			clubService.modify(targetClub);
			narrator.sayln("modified" + targetClub);
		} catch(IllegalArgumentException | NoSuchClubException e) {
			narrator.sayln(e.getMessage());
		}
	}
	public void remove() {
		TravelClubDto targetClub = findOne();
		if(targetClub == null) {
			return;
		}
		String confirmStr = consoleUtil.getValueOf("remove?");
		if(confirmStr.toLowerCase().equals("y") || confirmStr.toLowerCase().equals("yes")) {
			narrator.sayln("removing" + targetClub.getName());
			clubService.remove(targetClub.getUsid());
		} else {
			narrator.sayln("cancel" + targetClub.getName());
		}
	}
}


























