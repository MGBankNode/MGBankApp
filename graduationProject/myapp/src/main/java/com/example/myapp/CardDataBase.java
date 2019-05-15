package com.example.myapp;

import java.util.ArrayList;

public class CardDataBase {
    public ArrayList<CreditCard> CardList;

    /*카드 데이터 베이스 입력방법*/
    /*

    * 1. 혜택 집합 선언하기 (카드당 1개)
    * ArrayList<Benefits> 해당카드의혜택들의 집합 이름 = new ArrayList<Benefits>();
    * ex) ArrayList<Benefits> PeachCardBenefits = new ArrayList<Benefits>();
    *
    * 2. 혜택 집합에 혜택 추가하기 (횟수제한이나 금액제한이 없을시 int 인자값을 -1 로 준다.)
    * 해당카드혜택들의 집합 이름.add(new Benefits(String "혜택이름", new String[]{"혜택이", "적용", "될만한", "확실한", "문자열들"}
    *       , int (혜택 퍼센트, 금액은 그냥 숫자로 기입), int (일별 횟수제한), int (월별 횟수제한), int (연도별 횟수제한), int (건당 금액제한));
    * ex)PeachCardBenefits.add(new Benefits("PeachConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 5, 1, 2, 0, -1));
    *
    * 3. 카드리스트에 카드를 추가한다.
    * CardList.add(new CreditCard(String "카드이름(어플에 적용됨)", int 카드의 이미지(어플에 적용됨), ArrayList<Benefits> 혜택집합);
    * ex) CardList.add(new CreditCard("PeachCard", R.drawable.card_peach, PeachCardBenefits));
    *
    *
    *
    * */
    public CardDataBase(){
        CardList = new ArrayList<CreditCard>();

        ArrayList<Benefits> PeachCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> OnCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> MintCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> WithCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> LimeCardBenefits = new ArrayList<Benefits>();
        ArrayList<Benefits> KakaoCardBenefits = new ArrayList<>();
        ArrayList<Benefits> INCardBenefits = new ArrayList<>();
        ArrayList<Benefits> MarketBenefits = new ArrayList<>();
        ArrayList<Benefits> HomeShoppingBenefits = new ArrayList<>();
        ArrayList<Benefits> ForUCardBenefits = new ArrayList<>();
        ArrayList<Benefits> MgLifeBenefits = new ArrayList<>();

        //지현 WITH체크카드

        WithCardBenefits.add(new Benefits("WithCommunicate",
                new String[]{"SKT", "KT", "LGU+"}, 2000, -1, 1, -1, 50000));
        WithCardBenefits.add(new Benefits("WithHospital",
                new String[]{"병원", "의원", "의료원", "약국", "내과", "소아과", "성형"}, 5, -1, 2, 12, 50000));
        WithCardBenefits.add(new Benefits("WithGolf",
                new String[]{"골프"}, 5, -1, 1, -1, 50000));
        WithCardBenefits.add(new Benefits("WithMart",
                new String[]{"emart", "e마트", "이마트", "홈플러스", "homeplus", "롯데마트"}, 5, 1, 2, -1, 50000));
        WithCardBenefits.add(new Benefits("WithHotel",
                new String[]{"호텔", "콘도", "모텔"}, 5, -1, 2, 12, -1));
        WithCardBenefits.add(new Benefits("WithCar",
                new String[]{"자동차", "정비", "타이어"}, 5000, -1, -1, 2, 100000));
        WithCardBenefits.add(new Benefits("WithMovie",
                new String[]{"CGV", "megabox", "MEGABOX", "롯데시네마"}, 2000, -1, 1, -1, 5000));
        WithCardBenefits.add(new Benefits("WithTraffic",
                new String[]{"고속버스", "코레일", "KOBUS", "KORAIL", "kobus", "korail"}, 5, -1, 2, 12, -1));
        WithCardBenefits.add(new Benefits("WithBakery",
                new String[]{"파리바케트", "뚜레쥬르", "파리바게뜨", "뚜레주르"}, 10, -1, 1, -1, 10000));

        //On체크카드
        OnCardBenefits.add(new Benefits("OnOnline",
                new String[]{"카카오페이", "네이버페이", "PAYCO", "payco"}, 1000, -1, 4, -1, 10000));
        OnCardBenefits.add(new Benefits("OnOnlineMovie",
                new String[]{"CGV"}, 3000, -1, 1, -1, 20000));
        OnCardBenefits.add(new Benefits("OnOnline",
                new String[]{"TOEIC", "TEPS", "toeic", "teps", "토익", "텝스"}, 2000, -1, 2, -1, -1));

        //MINT체크카드
        MintCardBenefits.add(new Benefits("MintMart",
                new String[]{"emart", "e마트", "이마트", "홈플러스", "homeplus", "롯데마트"}, 5, 1, 2, -1, 50000));
        MintCardBenefits.add(new Benefits("MintConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 5, 1, 2, -1, 10000));
        MintCardBenefits.add(new Benefits("MintCommunicate",
                new String[]{"SKT", "KT", "LGU+"}, 5000, -1, 1, -1, 50000));
        MintCardBenefits.add(new Benefits("MintGolf",
                new String[]{"골프"}, 5, -1, 1, -1, 50000));
        MintCardBenefits.add(new Benefits("MintSports",
                new String[]{"스키", "볼링", "테니스", "수영", "헬스클럽", "레저"}, 5, -1, 1, -1, 50000));
        MintCardBenefits.add(new Benefits("MintMovie",
                new String[]{"CGV", "megabox", "MEGABOX", "롯데시네마"}, 2000, -1, 1, -1, 10000));
        MintCardBenefits.add(new Benefits("MintRestaurant",
                new String[]{"아웃백", "OUTBACK", "VIPS", "vips", "outback", "빕스"}, 10, -1, 1, -1, 60000));

        //LIME체크카드
        LimeCardBenefits.add(new Benefits("LimeLPG",
                new String[]{"SK에너지", "주유소", "GS칼텍스", "SOIL", "S-OIL", "s-oil", "soil", "에스오일"}, 3, -1, 4, -1, -1));
        LimeCardBenefits.add(new Benefits("LimeCoffee",
                new String[]{"스타벅스", "커피빈", "파스쿠찌"}, 1000, 1, 2, -1, 10000));
        LimeCardBenefits.add(new Benefits("LimeConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 1000, 1, 2, -1, 10000));



        //PeachCard
        //임시로 한 것
        PeachCardBenefits.add(new Benefits("PeachTheater",
                new String[]{"CGV", "씨지비", "megabpx", "MEGABOX", "메가박스", "롯데시네마"}, 2000, -1, 1, 0, 10000));
        PeachCardBenefits.add(new Benefits("PeachStore",
                new String[]{"롯데백화점", "신세계", "현대백화점"}, 7, 1, 2, -1, 50000));
        PeachCardBenefits.add(new Benefits("PeachOnline",
                new String[]{"위메프", "티몬", "TMON", "tmon", "coupang", "COUPANG", "쿠팡"}, 10, 1, 3, -1, 3000));
        PeachCardBenefits.add(new Benefits("PeachCoffee",
                new String[]{"스타벅스", "커피빈", "파스쿠찌"}, 1000, 1, 2, -1, 10000));
        PeachCardBenefits.add(new Benefits("PeachBeauty",
                new String[]{"헤어", "미용실", "올리브영", "Olive"}, 10, -1, 1, -1, 50000));
        PeachCardBenefits.add(new Benefits("PeachInterpark",
                new String[]{"인터파크"}, 5, 1, -1, -1, -1));
        PeachCardBenefits.add(new Benefits("PeachStudy",
                new String[]{"학원", "메가스터디", "에듀"}, 5, -1, 1, -1, 100000));




        //KakaoCard
        //7. KakaoCard//
        KakaoCardBenefits.add(new Benefits("KakaoConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유", "미니스톱"}, 5, -1, 2, -1, -1));

        KakaoCardBenefits.add(new Benefits("KakaoSocialCommerce",
                        new String[]{"TMON", "티몬", "coupang", "COUPANG", "쿠팡", "위메프"}, 10, 4, -1, -1, -1));

        KakaoCardBenefits.add(new Benefits("KakaoLanguageStudy",
                new String[]{"TEPS", "텝스", "TOEIC", "토익"}, 2000, -1, 2, 6 , -1));

        KakaoCardBenefits.add(new Benefits("KakaoTheater",
                new String[]{"CGV", "씨지비", "megabox", "MEGABOX", "메가박스", "롯데시네마"}, 2000, -1, 2, 12, 7000));

        KakaoCardBenefits.add(new Benefits("KakaoCoffee",
                new String[]{"STARBUCKS", "starbucks","스타벅스", "COFFEEBEAN", "coffeebean", "커피빈", "TOM N TOMS", "탐앤탐스", "CAFFEBENE", "caffebene", "카페베네"}, 10, -1, 2, 12, -1));

        KakaoCardBenefits.add(new Benefits("KakaoStyleFood",
                new String[]{"BURGERKING", "버거킹", "MCDONALD", "mcdonald", "맥도날드", "LOTTELIA", "lottelia", "롯데리아", "MISTERDONUT", "미스터도넛", "DUNKINDONUTS", "던킨도넛" }, 10, -1, 2, 12 ,-1));

        //KakaoCard//

        //8. INCard
        INCardBenefits.add(new Benefits("INCommunicationFare",
                new String[]{"SKT", "KT", "LGU+", "LG유플러스"}, 2000, -1, 1, -1, 30000));

        INCardBenefits.add(new Benefits("INAcademy",
                new String[]{"학원", "대성", "메가스터디"}, 5, -1, 1, 6, 50000));

        INCardBenefits.add(new Benefits("INMedical",
                new String[]{"병원", "약국", "의원"}, 5, -1, 2, 12, 30000));

        INCardBenefits.add(new Benefits("INShopping",
                new String[]{"G마켓", "GMarket", "옥션", "AUCTION", "11번가", "11st", "LOTTECOM", "롯데닷컴", "GSSHOP", "GS샵", "CJmall", "씨제이몰", "신세계몰", "SHINSEGAEMALL"}, 5, 1, 2, -1, 30000));

        INCardBenefits.add(new Benefits("INDepartmentStore",
                new String[]{"롯데백화점", "신세계백화점", "현대백화점", "emart", "LOTTEMART", "LOTTEMart", "롯데마트"}, 5, 1,2, -1, 30000));

        INCardBenefits.add(new Benefits("INTheater",
                new String[]{"CGV", "씨지비", "megabox", "MEGABOX", "메가박스", "롯데시네마"}, 2000, -1, 1, -1, 5000));

        INCardBenefits.add(new Benefits("INBook",
                new String[]{"YES24", "예스24", "yes24", "교보문고", "KYOBO문고", "알라딘", "영풍문고", "YPBOOKS", "리브로", "Libro"}, 2000, 1, -1, 6, 30000));

        INCardBenefits.add(new Benefits("INShow",
                new String[]{"YES24 공연", "인터파크 공연"}, 3, -1, 1, 6, -1));
        //INCard

        //9. MarketCard
        MarketBenefits.add(new Benefits("MarketCommunicationFare",
                new String[]{"SKT", "KT", "LGU+", "LG유플러스"}, 2000, -1, 1, -1, 30000));

        MarketBenefits.add(new Benefits("MarketTransportationFee",
                new String[]{"코레일", "KORAIL", "korali", "고속버스", "KOBUS", "kobus"}, 5, -1, 1, 6, -1));

        //MarketCard

        //10. HomeshoppingCard
        HomeShoppingBenefits.add(new Benefits("Homeshopping",
                new String[]{"CJ오쇼핑", "GS SHOP", "GSSHOP", "현대Hmall", "롯데홈쇼핑", "NS홈쇼핑"}, 6, -1, -1, -1, 180000));

        HomeShoppingBenefits.add(new Benefits("HomeshoppingDepartment",
                new String[]{"롯데백화점", "신세계백화점", "현대백화점"}, 5, -1,2, 12, 100000));

        HomeShoppingBenefits.add(new Benefits("HomeshoppingBakery",
                new String[]{"파리바케트", "파리바게뜨", "PARIS BAGUETTE", "뚜레쥬르", "TOUSlesJOURS"}, 10, -1, 2, 12 , 10000));

        HomeShoppingBenefits.add(new Benefits("HomeShoppingCommunicationFare",
                new String[]{"SKT", "KT", "LGU+", "LG유플러스"}, 5000, -1, 1, 12, 50000));

        //HomeshoppingCard

        //11. ForUCard
        ForUCardBenefits.add(new Benefits("ForUOil",
                new String[]{"SK에너지", "현대오일", "GS칼텍스", "S-OIL", "S-oil", "NH 알뜰주유소"}, 3, -1, 4, -1, -1));

        ForUCardBenefits.add(new Benefits("ForUBook",
                new String[]{"YES24", "예스24", "yes24", "교보문고", "알라딘", "영풍문고"}, 2000, -1, 2, 6, 30000));

        ForUCardBenefits.add(new Benefits("ForUCoffee",
                new String[]{"STARBUCKS", "starbucks","스타벅스", "COFFEEBEAN", "coffeebean", "커피빈", "TOM N TOMS", "탐앤탐스", "CAFFEBENE", "caffebene", "카페베네"}, 10, 1, 2, -1, -1));

        ForUCardBenefits.add(new Benefits("ForUSuperMarket",
                new String[]{"emart", "이마트", "홈플러스", "Home plus", "롯데마트"}, 5, 1, 2, -1, 30000));

        ForUCardBenefits.add(new Benefits("ForUConvenience",
                new String[]{"GS25", "지에스25", "쥐에스25", "세븐일레븐", "CU", "씨유"}, 5, -1, 2, 12, -1));

        //ForUCard

        //12. MgLifeCard
        MgLifeBenefits.add(new Benefits("MgLifeCoffee",
                new String[]{"STARBUCKS", "starbucks","스타벅스", "COFFEEBEAN", "coffeebean", "커피빈", "TOM N TOMS", "탐앤탐스", "CAFFEBENE", "caffebene", "카페베네"}, 10, 1, 2, -1, -1));

        MgLifeBenefits.add(new Benefits("MgLifeTheater",
                new String[]{"CGV", "씨지비", "megabox", "MEGABOX", "메가박스", "롯데시네마"}, 4000, -1, 2, 8, 10000));

        MgLifeBenefits.add(new Benefits("MgLifeShow",
                new String[]{"YES24 공연", "인터파크 공연"}, 3, -1, 1, 6, -1));

        MgLifeBenefits.add(new Benefits("MgLifeLanguageStudy",
                new String[]{"TEPS", "텝스", "TOEIC", "토익"}, 2000, -1, 1, 6 , -1));

        MgLifeBenefits.add(new Benefits("MgLifeLanguageStudyBook",
                new String[]{"YES24", "예스24", "yes24", "교보문고", "알라딘", "영풍문고"}, 2000, 1, 0, 6, 30000));

        MgLifeBenefits.add(new Benefits("MgLifeDepartmentStore",
                new String[]{"롯데백화점", "신세계백화점", "현대백화점"}, 5, 1,2, -1, 30000));

        MgLifeBenefits.add(new Benefits("MgLifeSocialCommerce",
                new String[]{"TMON", "티몬", "coupang", "COUPANG", "쿠팡", "위메프"}, 10, -1, 2, 10, -1));
        //MgLifeCard


       
        CardList.add(new CreditCard("MINT 체크카드", R.drawable.card_mint, MintCardBenefits));
        CardList.add(new CreditCard("PEACH 체크카드", R.drawable.card_peach, PeachCardBenefits));
        CardList.add(new CreditCard("LIME 체크카드", R.drawable.card_lime, LimeCardBenefits));
        CardList.add(new CreditCard( "WITH 체크카드", R.drawable.card_with, WithCardBenefits));
        CardList.add(new CreditCard("ON 체크카드", R.drawable.card_on, OnCardBenefits));
        CardList.add(new CreditCard("카카오페이 체크카드", R.drawable.card_kakao, KakaoCardBenefits));
        CardList.add(new CreditCard("IN 체크카드", R.drawable.card_in,INCardBenefits));
        CardList.add(new CreditCard("시장 체크카드",R.drawable.card_market,MarketBenefits));
        CardList.add(new CreditCard("부자되세요 체크카드",R.drawable.card_homeshopping, HomeShoppingBenefits));
        CardList.add(new CreditCard("ForU 체크카드",R.drawable.card_foru, ForUCardBenefits));
        CardList.add(new CreditCard("MgLife 체크카드", R.drawable.card_mglife, MgLifeBenefits));

    } 

}
