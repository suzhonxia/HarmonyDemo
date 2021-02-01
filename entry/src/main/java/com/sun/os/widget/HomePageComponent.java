package com.sun.os.widget;

import com.sun.os.ResourceTable;
import com.sun.os.bean.ContestBean;
import com.sun.os.bean.NewsBean;
import com.sun.os.tool.*;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;

public class HomePageComponent extends ComponentContainer {

    private int scrolledX = 0;

    public HomePageComponent(Context context) {
        super(context);
        init();
    }

    public HomePageComponent(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init();
    }

    public HomePageComponent(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init();
    }

    private void init() {
        Component contentLayout = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_layout_home_page, this, false);
        addComponent(contentLayout);

        initContentScroll(contentLayout);
        initBannerLayout(contentLayout);
        initNoticeLayout(contentLayout);
        initPosterView(contentLayout);
        initPremiumLayout(contentLayout);
        initSeriesLayout(contentLayout);
        initPartLayout(contentLayout);
        initEducLayout(contentLayout);
        initContestLayout(contentLayout);
        initNewsLayout(contentLayout);
    }

    private void initContentScroll(Component contentLayout) {
        ScrollView contentScroll = (ScrollView) contentLayout.findComponentById(ResourceTable.Id_contentScroll);
        contentScroll.setReboundEffect(true);
        contentScroll.setReboundEffectParams(5, 0.1F, 80);
    }

    private void initBannerLayout(Component contentLayout) {
        int space = vp2px(16);
        int width = DisplayUtils.getDisplayWidthInPx(getContext()) - space * 2;
        int height = (int) (width * 0.44);
        Logger.logI("initBannerLayout width = " + width + ", height = " + height);
        int[] bannerList = {ResourceTable.Media_img_banner_1, ResourceTable.Media_img_banner_2,
                ResourceTable.Media_img_banner_1, ResourceTable.Media_img_banner_2, ResourceTable.Media_img_banner_1};

        initPointLayout(contentLayout, 0, bannerList.length);
        DirectionalLayout bannerLayout = (DirectionalLayout) contentLayout.findComponentById(ResourceTable.Id_bannerLayout);
        for (int i = 0; i < bannerList.length; i++) {
            Image ivBanner = new Image(getContext());
            ivBanner.setPixelMap(bannerList[i]);
            ivBanner.setScaleMode(Image.ScaleMode.CLIP_CENTER);
            ivBanner.setCornerRadius(vp2px(5));

            LayoutConfig layoutConfig = new LayoutConfig(width, height);
            if (i == 0) {
                layoutConfig.setMarginLeft(space);
                layoutConfig.setMarginRight(space / 2);
            } else if (i == bannerList.length - 1) {
                layoutConfig.setMarginRight(space);
            } else {
                layoutConfig.setMarginRight(space / 2);
            }

            bannerLayout.addComponent(ivBanner, layoutConfig);
        }

        ScrollView bannerScroll = (ScrollView) contentLayout.findComponentById(ResourceTable.Id_bannerScroll);
        bannerScroll.setScrolledListener((component, scrollX, scrollY, oldScrollX, oldScrollY) -> scrolledX = scrollX);
        bannerScroll.setTouchEventListener((component, touchEvent) -> {
            if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_UP || touchEvent.getAction() == TouchEvent.CANCEL) {
                int distance = width + space / 2;
                int index = scrolledX / distance;
                int offsetX = scrolledX % distance;
                Logger.logI("initBannerLayout onTouchEvent scrolledX = " + scrolledX + ", distance = " + distance + ", index = " + index + ", offsetX = " + offsetX);

                if (offsetX < (distance / 2)) {
                    // 回到上一个索引位
                    bannerScroll.fluentScrollXTo(index * distance);
                    initPointLayout(contentLayout, index, bannerList.length);
                } else {
                    // 滚动到下一个索引位
                    bannerScroll.fluentScrollXTo((index + 1) * distance);
                    initPointLayout(contentLayout, index + 1, bannerList.length);
                }
            }
            return false;
        });
    }

    private void initPointLayout(Component contentLayout, int index, int size) {
        DirectionalLayout pointLayout = (DirectionalLayout) contentLayout.findComponentById(ResourceTable.Id_pointLayout);
        pointLayout.removeAllComponents();
        for (int i = 0; i < size; i++) {
            int res = i == index ? ResourceTable.Graphic_banner_point_select_tablet : ResourceTable.Graphic_banner_point_normal_tablet;
            Component point = new Component(getContext());
            point.setBackground(new ShapeElementCreator.Builder(getContext(), res).build());

            LayoutConfig layoutConfig = new LayoutConfig(vp2px(i == index ? 16 : 6), vp2px(6));
            if (i != 0) {
                layoutConfig.setMarginLeft(vp2px(4));
            }

            pointLayout.addComponent(point, layoutConfig);
        }
    }

    private void initNoticeLayout(Component contentLayout) {
        Text tvNotice = (Text) contentLayout.findComponentById(ResourceTable.Id_tvNotice);
        tvNotice.setTruncationMode(Text.TruncationMode.AUTO_SCROLLING);
        tvNotice.setAutoScrollingCount(Text.AUTO_SCROLLING_FOREVER);
        tvNotice.startAutoScrolling();
    }

    private void initPosterView(Component contentLayout) {
        int width = DisplayUtils.getDisplayWidthInPx(getContext()) - vp2px(15) * 2;
        Image ivPoster = (Image) contentLayout.findComponentById(ResourceTable.Id_ivPoster);
        ivPoster.setHeight((int) (width * 0.173333));
        HmOSImageLoader.with(getContext()).load(Constant.posterPath).def(ResourceTable.Media_img_poster).into(ivPoster);
    }

    private void initPremiumLayout(Component contentLayout) {
        HomePageTitleComponent titleComponent = (HomePageTitleComponent) contentLayout.findComponentById(ResourceTable.Id_titlePremium);
        titleComponent.setData("精品付费课", "查看全部＞", null);
    }

    private void initSeriesLayout(Component contentLayout) {
        HomePageTitleComponent titleComponent = (HomePageTitleComponent) contentLayout.findComponentById(ResourceTable.Id_titleSeries);
        titleComponent.setData("创造力培养体系", "", null);
    }

    private void initPartLayout(Component contentLayout) {
        HomePageTitleComponent titleComponent = (HomePageTitleComponent) contentLayout.findComponentById(ResourceTable.Id_titlePart);
        titleComponent.setData("配件体系课", "查看全部＞", null);
    }

    private void initEducLayout(Component contentLayout) {
        HomePageTitleComponent titleComponent = (HomePageTitleComponent) contentLayout.findComponentById(ResourceTable.Id_titleEduc);
        titleComponent.setData("亲职教育", "", null);
    }

    private void initContestLayout(Component contentLayout) {
        HomePageTitleComponent titleComponent = (HomePageTitleComponent) contentLayout.findComponentById(ResourceTable.Id_titleContest);
        titleComponent.setData("竞赛", "查看全部＞", null);

        ArrayList<ContestBean> contestList = new ArrayList<>();
        contestList.add(new ContestBean("创意造型PK", "海选已结束", ResourceTable.Media_img_home_contest));

        DirectionalLayout contestContainer = (DirectionalLayout) contentLayout.findComponentById(ResourceTable.Id_contestContainer);
        for (ContestBean contestBean : contestList) {
            Component contestComponent = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_layout_home_page_contest, contestContainer, false);

            Image ivContestCover = (Image) contestComponent.findComponentById(ResourceTable.Id_ivContestCover);
            ivContestCover.setHeight((int) ((DisplayUtils.getDisplayWidthInPx(getContext()) - vp2px(60)) * 0.45));
            ivContestCover.setCornerRadii(new float[]{vp2px(5), vp2px(5), vp2px(5), vp2px(5), 0, 0, 0, 0});
            ivContestCover.setPixelMap(contestBean.cover);

            Text tvContestLabel = ((Text) contestComponent.findComponentById(ResourceTable.Id_tvContestLabel));
            tvContestLabel.setText(contestBean.label);
            tvContestLabel.setBackground(new ShapeElementCreator.Builder(getContext(), ResourceTable.Graphic_shape_contest_label_bg)
                    .setShape(ShapeElement.RECTANGLE).setCornerRadiiArray(vp2px(5), vp2px(9), vp2px(9), vp2px(0)).build());

            Text tvContestTitle = (Text) contestComponent.findComponentById(ResourceTable.Id_tvContestTitle);
            tvContestTitle.setText(contestBean.title);

            contestContainer.addComponent(contestComponent);
        }
    }

    private void initNewsLayout(Component contentLayout) {
        HomePageTitleComponent titleComponent = (HomePageTitleComponent) contentLayout.findComponentById(ResourceTable.Id_titleNews);
        titleComponent.setData("学院资讯", "更多＞", null);

        ArrayList<NewsBean> newsList = new ArrayList<>();
        newsList.add(new NewsBean("关于磁力片边缘飞起瑕疵的工艺说明,如果你看了就懂了", "2020-02-27", "10834", true));
        newsList.add(new NewsBean("App 的使用方法", "2020-07-01", "499", false));
        newsList.add(new NewsBean("磁力片的起源", "2019-12-11", "93", false));

        DirectionalLayout newsContainer = (DirectionalLayout) contentLayout.findComponentById(ResourceTable.Id_newsContainer);
        for (NewsBean newsBean : newsList) {
            Component newsComponent = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_layout_home_page_news, newsContainer, false);
            ((Text) newsComponent.findComponentById(ResourceTable.Id_tvNewsTitle)).setText((newsBean.top ? "【置顶】" : "") + newsBean.title);
            ((Text) newsComponent.findComponentById(ResourceTable.Id_tvNewsDate)).setText(newsBean.dateTime);
            ((Text) newsComponent.findComponentById(ResourceTable.Id_tvNewsCount)).setText(newsBean.count);

            newsContainer.addComponent(newsComponent);
        }
    }

    private int vp2px(int vp) {
        return DisplayUtils.vp2px(getContext(), vp);
    }
}