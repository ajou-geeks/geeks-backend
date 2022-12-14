package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTemplate {

    GROUP_BUYING_JOIN_01("공동구매에 참여했어요.", "[%s]님이 [%s]에 참여했어요."),

    GROUP_BUYING_CLOSE_01("공동구매가 마감되었어요.", "[%s]의 구매를 진행하고 정산을 요청해주세요."),
    GROUP_BUYING_CLOSE_02("공동구매가 마감되었어요.", "[%s]의 구매가 진행되고 있으니 조금만 기다려주세요."),

//    GROUP_BUYING_EXPIRE_01("공동구매가 만료되었어요.", "아쉽지만 [%s] 대신 다른 공동구매를 생성해보시는 것은 어떠신가요?"),
//    GROUP_BUYING_EXPIRE_02("공동구매가 만료되었어요.", "아쉽지만 [%s] 대신 다른 공동구매에 참여해보시는 것은 어떠신가요?"),

    GROUP_BUYING_SETTLE_01("공동구매 정산이 요청되었어요.", "[%s]의 정산을 위해 %s %s로 %s원을 송금해주세요."),

    GROUP_BUYING_RECEIVE_01("공동구매 물건을 수령하세요.", "[%s]의 수령을 위해 %s에 %s에서 만나요!");

    private final String title;
    private final String content;
}
