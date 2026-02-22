package kr.co.noir.mypageReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
@Service
public class ReserveEmailService {

	

	    @Autowired
	    @Qualifier("brevoMailSender")
	    private JavaMailSender brevoMailSender;

	    public void sendHotelReserveMail(List<HotelRevDetailDomain> details) {
	        try {
	            MimeMessage message = brevoMailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	            HotelRevDetailDomain main = details.get(0);
	            helper.setFrom("mellifluous5473@gmail.com", "NOIR HOTEL");
	            helper.setTo(main.getEmail()); 
	            helper.setSubject("[NOIR HOTEL] 예약 확정 안내 - No." + main.getReserveNum());

	            // 1. 방 목록 반복 생성 (HTML의 카드 디자인 복제)
	            StringBuilder roomCardsHtml = new StringBuilder();
	            for (HotelRevDetailDomain room : details) {
	                roomCardsHtml.append(
	                    "<div style='border: 1px solid #2a2a2a; border-radius: 12px; padding: 30px; margin-bottom: 25px; background-color: transparent;'>" +
	                        "<div style='color: #777; font-size: 11px; text-transform: uppercase; letter-spacing: 1.5px; margin-bottom: 8px;'>Room Type</div>" +
	                        "<div style='font-size: 22px; color: #ffffff; font-weight: bold; text-transform: uppercase; margin-bottom: 25px;'>" + room.getRoomType() + "</div>" +
	                        
	                        "<div style='color: #777; font-size: 11px; text-transform: uppercase; letter-spacing: 1.5px; margin-bottom: 8px;'>Stay Period</div>" +
	                        "<div style='font-size: 16px; color: #efefef;'>" +
	                            "<span>" + room.getStartDate() + "</span> <span style='color:#555; font-size: 13px;'>" + room.getCheckInTime() + "</span>" +
	                            "<span style='color: #c5a47e; margin: 0 15px;'>&rarr;</span>" +
	                            "<span>" + room.getEndDate() + "</span> <span style='color:#555; font-size: 13px;'>" + room.getCheckOutTime() + "</span>" +
	                        "</div>" +
	                    "</div>"
	                );
	            }

	            // 2. 전체 레이아웃 (웹 상세 페이지 스타일 이식)
	            String htmlContent = 
	                "<div style='background-color: #0b0b0b; padding: 50px 20px; font-family: \"Nanum Myeongjo\", serif; color: #ffffff;'>" +
	                    "<div style='max-width: 750px; margin: 0 auto; background-color: #000000; border: 1px solid #2a2a2a; padding: 60px; position: relative;'>" +
	                        
	                        "" +
	                        "<div style='text-align: center; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 40px; margin-bottom: 50px;'>" +
	                            "<div style='background-color: #c5a47e; color: #000; padding: 6px 20px; font-size: 12px; font-weight: bold; display: inline-block; letter-spacing: 2px; margin-bottom: 20px;'>RESERVATION CONFIRMED</div>" +
	                            "<h2 style='font-size: 32px; letter-spacing: 3px; margin: 0; color: #ffffff;'>YOUR STAY AT NOIR HOTEL</h2>" +
	                            "<div style='color: #c5a47e; font-size: 16px; margin-top: 15px; letter-spacing: 1px;'>" +
	                                "No." + main.getReserveNum() + " | " + main.getReserveName() + " 님" +
	                            "</div>" +
	                        "</div>" +

	                        "" +
	                        roomCardsHtml.toString() +

	                        "" +
	                        "<table style='width: 100%; margin-top: 40px; border-collapse: collapse;'>" +
	                            "<tr>" +
	                                "<td style='width: 50%; vertical-align: top; padding-right: 20px;'>" +
	                                    "<div style='color: #777; font-size: 11px; text-transform: uppercase; margin-bottom: 5px;'>Guest Count</div>" +
	                                    "<div style='color: #efefef; font-size: 16px; margin-bottom: 25px;'>성인 " + main.getAdultCount() + " / 아동 " + main.getChildCount() + "</div>" +
	                                    "<div style='color: #777; font-size: 11px; text-transform: uppercase; margin-bottom: 5px;'>Special Request</div>" +
	                                    "<div style='color: #888; font-size: 14px; font-style: italic; line-height: 1.6;'>\"" + (main.getReserveMsg() != null ? main.getReserveMsg() : "No special requests.") + "\"</div>" +
	                                "</td>" +
	                                "<td style='width: 50%; vertical-align: top; border-left: 1px solid rgba(255,255,255,0.1); padding-left: 40px;'>" +
	                                    "<div style='color: #777; font-size: 11px; text-transform: uppercase; margin-bottom: 5px;'>Contact</div>" +
	                                    "<div style='color: #efefef; font-size: 15px; margin-bottom: 15px;'>" + main.getEmail() + "</div>" +
	                                    "<div style='color: #777; font-size: 11px; text-transform: uppercase; margin-bottom: 5px;'>TEL</div>" +
	                                    "<div style='color: #efefef; font-size: 15px;'>" + main.getTel() + "</div>" +
	                                "</td>" +
	                            "</tr>" +
	                        "</table>" +

	                        "" +
	                        "<div style='margin-top: 50px; padding: 35px; background-color: #1d1d1d; border: 1px solid #2a2a2a; border-radius: 4px;'>" +
	                            "<div style='color: #c5a47e; font-size: 13px; font-weight: bold; margin-bottom: 20px;'>PAYMENT INFORMATION</div>" +
	                            "<div style='display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 14px; color: #888;'>" +
	                                "<span>Room Rate</span>" +
	                                "<span style='color: #fff;'>KRW " + String.format("%,d", main.getRoomPrice()) + "</span>" +
	                            "</div>" +
	                            "<div style='display: flex; justify-content: space-between; margin-bottom: 20px; font-size: 14px; color: #888;'>" +
	                                "<span>Tax & Service Charge</span>" +
	                                "<span style='color: #fff;'>KRW " + String.format("%,d", main.getRoomTax()) + "</span>" +
	                            "</div>" +
	                            "<div style='border-top: 1px solid #333; padding-top: 20px; display: flex; justify-content: space-between;'>" +
	                                "<span style='color: #c5a47e; font-weight: bold; font-size: 13px;'>TOTAL AMOUNT</span>" +
	                                "<span style='color: #c5a47e; font-weight: bold; font-size: 26px;'>KRW " + String.format("%,d", main.getRoomPriceTotal()) + "</span>" +
	                            "</div>" +
	                            "<div style='color: #555; font-size: 11px; margin-top: 15px; text-align: right;'>" +
	                                "Paid via " + main.getPayAgency() + " on " + main.getPayInputDate() +
	                            "</div>" +
	                        "</div>" +

	                        "" +
	                        "<div style='margin-top: 50px; padding: 30px; background-color: #0f0f0f; border-left: 3px solid #c5a47e;'>" +
	                            "<h6 style='color: #c5a47e; margin: 0 0 15px 0; font-size: 14px;'>Hotel Policy & Cancellation</h6>" +
	                            "<ul style='color: #777; font-size: 12px; padding-left: 18px; line-height: 2; margin: 0;'>" +
	                                "<li>체크인 시 신분증과 예약 번호 제시가 필요합니다.</li>" +
	                                "<li>체크인 2일 전까지 무료 취소 가능, 이후 50% 수수료가 발생합니다.</li>" +
	                                "<li>당일 취소 및 노쇼(No-show) 시 환불이 불가합니다.</li>" +
	                            "</ul>" +
	                        "</div>" +

	                        "" +
	                        "<div style='margin-top: 60px; text-align: center; color: #444; font-size: 11px; letter-spacing: 1px;'>" +
	                            "NOIR HOTEL SEOUL | 123 Luxury Road, Gangnam <br>" +
	                            "本 메일은 시스템 발신전용입니다." +
	                        "</div>" +
	                    "</div>" +
	                "</div>";

	            helper.setText(htmlContent, true);
	            brevoMailSender.send(message);

	        } catch (Exception e) {
	            throw new RuntimeException("메일 발송 실패", e);
	        }
	    }
	    
	    
	    
	    public void sendDiningReserveMail(DinningRevDetailDomain dining) {
	        try {
	            MimeMessage message = brevoMailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	            helper.setFrom("mellifluous5473@gmail.com", "NOIR HOTEL");
	            helper.setTo(dining.getEmail()); 
	            helper.setSubject("[NOIR HOTEL] 다이닝 예약 확인서 - No." + dining.getReserveNum());


	            // 데이터 가공
	            String formattedTotal = String.format("%,d", dining.getRoomPriceTotal());
	            String cardInfo = dining.getCardData(); 

	            String htmlContent = 
	                    "<div style='background-color: #111; padding: 50px 20px; font-family: \"Nanum Myeongjo\", serif;'>" +
	                        "" +
	                        "<div style='max-width: 650px; margin: 0 auto; background-color: #000; border: 1px solid #c5a47e; position: relative;'>" +
	                            
	                            "" +
	                            "<div style='height: 4px; background-color: #c5a47e;'></div>" +

	                            "<div style='padding: 60px 50px;'>" +
	                                "" +
	                                "<div style='text-align: center; margin-bottom: 50px;'>" +
	                                    "<div style='font-size: 11px; letter-spacing: 5px; color: #c5a47e; margin-bottom: 15px;'>PRIVATE DINING SERVICE</div>" +
	                                    "<h1 style='font-size: 34px; color: #fff; margin: 0; font-weight: 300; letter-spacing: 8px;'>THE NOIR</h1>" +
	                                    "<p style='color: #888; font-size: 14px; margin-top: 20px; letter-spacing: 1px;'>고객님의 소중한 시간을 더 노아(THE NOIR)와 함께해주셔서 감사합니다.</p>" +
	                                "</div>" +

	                                "" +
	                                "<div style='border-top: 1px solid #333; border-bottom: 1px solid #333; padding: 40px 0; text-align: center;'>" +
	                                    "<div style='font-size: 13px; color: #c5a47e; margin-bottom: 15px;'>RESERVATION DETAILS</div>" +
	                                    "<h2 style='font-size: 26px; color: #fff; margin: 0; font-weight: 400;'>" + dining.getVisitDate() + " <span style='color: #444; font-weight: 100;'>|</span> " + dining.getVisitTime() + "</h2>" +
	                                    "<p style='font-size: 18px; color: #efefef; margin-top: 15px;'>" + dining.getDinningName() + "</p>" +
	                                    "<p style='font-size: 15px; color: #888; margin-top: 5px;'>" + dining.getDinningType() + " · 성인 " + dining.getAdultCount() + "명 / 아동 " + dining.getChildCount() + "명</p>" +
	                                "</div>" +

	                                "" +
	                                "<div style='margin-top: 40px;'>" +
	                                    "<table style='width: 100%; border-collapse: collapse; font-size: 15px; color: #bbb;'>" +
	                                        "<tr>" +
	                                            "<td style='padding: 15px 0; width: 120px; color: #666; font-size: 12px;'>RESERVATION NO.</td>" +
	                                            "<td style='padding: 15px 0; color: #fff; font-weight: bold;'>" + dining.getReserveNum() + "</td>" +
	                                        "</tr>" +
	                                        "<tr>" +
	                                            "<td style='padding: 15px 0; color: #666; font-size: 12px;'>GUEST NAME</td>" +
	                                            "<td style='padding: 15px 0; color: #fff;'>" + dining.getReserveName() + " 님</td>" +
	                                        "</tr>" +
	                                        "<tr>" +
	                                            "<td style='padding: 15px 0; color: #666; font-size: 12px; vertical-align: top;'>SPECIAL MSG</td>" +
	                                            "<td style='padding: 15px 0; color: #888; line-height: 1.6;'>" + (dining.getReserveMsg() != null ? dining.getReserveMsg() : "-") + "</td>" +
	                                        "</tr>" +
	                                    "</table>" +
	                                "</div>" +

	                                "" +
	                                "<div style='margin-top: 40px; background-color: #111; padding: 30px; border: 1px solid #222;'>" +
	                                    "<table style='width: 100%;'>" +
	                                        "<tr>" +
	                                            "<td style='color: #888; font-size: 13px;'>METHOD</td>" +
	                                            "<td style='text-align: right; color: #efefef; font-size: 13px;'>" + dining.getPayAgency() + " (" + cardInfo + ")</td>" +
	                                        "</tr>" +
	                                        "<tr>" +
	                                            "<td style='padding-top: 15px; color: #c5a47e; font-size: 14px; font-weight: bold;'>TOTAL AMOUNT</td>" +
	                                            "<td style='padding-top: 15px; text-align: right; color: #c5a47e; font-size: 24px; font-weight: bold;'>KRW " + formattedTotal + "</td>" +
	                                        "</tr>" +
	                                    "</table>" +
	                                "</div>" +

	                                "" +
	                                "<div style='margin-top: 50px; padding-top: 30px; border-top: 1px solid #222;'>" +
	                                    "<p style='color: #c5a47e; font-size: 13px; font-weight: bold; margin-bottom: 15px;'>NOTICE</p>" +
	                                    "<div style='font-size: 12px; color: #666; line-height: 2;'>" +
	                                        "• 원활한 서비스를 위해 예약 시간 <span style='color:#888;'>15분 경과 시 예약이 자동 취소</span>될 수 있습니다.<br>" +
	                                        "• 당일 취소 시 예약금 환불이 불가하오니 신중한 일정 확인 부탁드립니다.<br>" +
	                                        "• 알레르기 등 특이사항은 방문 전 매장으로 연락주시면 세심하게 준비하겠습니다." +
	                                    "</div>" +
	                                "</div>" +
	                            "</div>" +

	                            "" +
	                            "<div style='background-color: #0a0a0a; padding: 30px; text-align: center; border-top: 1px solid #222;'>" +
	                                "<div style='color: #444; font-size: 11px; letter-spacing: 2px;'>" +
	                                    "THE NOIR HOTEL & RESTAURANT<br>" +
	                                    "서울특별시 강남구 노아로 123  |  T. 02-123-4567" +
	                                "</div>" +
	                            "</div>" +
	                        "</div>" +
	                    "</div>";

	            helper.setText(htmlContent, true);
	            brevoMailSender.send(message);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
