package com.sun.os.widget;

import com.sun.os.ResourceTable;
import com.sun.os.bean.*;
import com.sun.os.tool.*;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
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

        int topMargin = (int) ((DisplayUtils.getDisplayWidthInPx(getContext()) - vp2px(60)) * 0.45F);
        ArrayList<PremiumBean> premiumList = new ArrayList<>();
        premiumList.add(new PremiumBean("磁力星球 早教课", "早教益智", 199, 298, ResourceTable.Media_img_home_premium_1));
        premiumList.add(new PremiumBean("大颗粒创意机械课 探索机械结构原理", "创意机械", 297, 699, ResourceTable.Media_img_home_premium_2));

        DirectionalLayout premiumContainer = (DirectionalLayout) contentLayout.findComponentById(ResourceTable.Id_premiumContainer);
        for (int i = 0; i < premiumList.size(); i++) {
            PremiumBean premiumBean = premiumList.get(i);
            Component premiumComponent = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_layout_home_page_premium, premiumContainer, false);

            premiumComponent.findComponentById(ResourceTable.Id_premiumContentLayout).getLayoutConfig().setMarginTop(topMargin);
            ((Text) premiumComponent.findComponentById(ResourceTable.Id_tvPremiumTitle)).setText(premiumBean.title);
            ((Text) premiumComponent.findComponentById(ResourceTable.Id_tvPremiumPrice)).setText("￥" + premiumBean.sellingPrice);
            ((Text) premiumComponent.findComponentById(ResourceTable.Id_tvPremiumOriginalPrice)).setText("￥" + premiumBean.originalPrice);

            Image ivPremiumCover = (Image) premiumComponent.findComponentById(ResourceTable.Id_ivPremiumCover);
            ivPremiumCover.setHeight((int) ((DisplayUtils.getDisplayWidthInPx(getContext()) - vp2px(60)) * 0.5625));
            ivPremiumCover.setCornerRadii(new float[]{vp2px(5), vp2px(5), vp2px(5), vp2px(5), 0, 0, 0, 0});
            ivPremiumCover.setPixelMap(premiumBean.cover);

            Text tvPremiumLabel = (Text) premiumComponent.findComponentById(ResourceTable.Id_tvPremiumLabel);
            tvPremiumLabel.setText(premiumBean.label);
            tvPremiumLabel.setBackground(new ShapeElementCreator.Builder(getContext(), ResourceTable.Graphic_shape_premium_label_bg)
                    .setShape(ShapeElement.RECTANGLE).setCornerRadiiArray(vp2px(5), vp2px(9), vp2px(9), vp2px(0)).build());

            LayoutConfig layoutConfig = new LayoutConfig();
            if (i != 0) {
                layoutConfig.setMarginTop(vp2px(15));
            }

            premiumContainer.addComponent(premiumComponent, layoutConfig);
        }
    }

    private void initSeriesLayout(Component contentLayout) {
        HomePageTitleComponent titleComponent = (HomePageTitleComponent) contentLayout.findComponentById(ResourceTable.Id_titleSeries);
        titleComponent.setData("创造力培养体系", "", null);

        ArrayList<SeriesBean> seriesList = new ArrayList<>();
        seriesList.add(new SeriesBean("融合积木 手工 亲子游戏的趣味立体式阅读课", new String[]{"亲子阅读", "益智建构", "绘本"}, ResourceTable.Media_img_home_series_1));
        seriesList.add(new SeriesBean("科学探索全能专家课  实现静态到电动全覆盖", new String[]{"科学启蒙", "适用314件"}, ResourceTable.Media_img_home_series_2));
        seriesList.add(new SeriesBean("大颗粒编程启蒙学习课程 开发思维潜能", new String[]{"153件", "实物编程"}, ResourceTable.Media_img_home_series_3));

        DirectionalLayout seriesContainer = (DirectionalLayout) contentLayout.findComponentById(ResourceTable.Id_seriesContainer);
        for (int i = 0; i < seriesList.size(); i++) {
            SeriesBean seriesBean = seriesList.get(i);
            Component seriesComponent = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_layout_home_page_series, seriesContainer, false);

            ((Text) seriesComponent.findComponentById(ResourceTable.Id_tvSeriesTitle)).setText(seriesBean.title);
            Image ivSeriesCover = (Image) seriesComponent.findComponentById(ResourceTable.Id_ivSeriesCover);
            ivSeriesCover.setCornerRadius(vp2px(5));
            ivSeriesCover.setPixelMap(seriesBean.cover);

            DirectionalLayout labelSeriesContainer = (DirectionalLayout) seriesComponent.findComponentById(ResourceTable.Id_labelSeriesContainer);
            for (int j = 0; j < seriesBean.labels.length; j++) {
                LayoutConfig layoutConfig = new LayoutConfig(LayoutConfig.MATCH_CONTENT, vp2px(20));
                if (j != 0) {
                    layoutConfig.setMarginLeft(vp2px(6));
                }

                Text label = new Text(getContext());
                label.setText(seriesBean.labels[j]);
                label.setLayoutConfig(layoutConfig);
                label.setTextSize(10, Text.TextSizeType.FP);
                label.setPadding(vp2px(8), 0, vp2px(8), 0);
                label.setTextColor(new Color(Color.getIntColor("#FFA200")));
                label.setBackground(new ShapeElementCreator.Builder().setShape(ShapeElement.RECTANGLE)
                        .setColor(0xFFFFF4D0).setCornerRadiiArray(0, vp2px(10), vp2px(10), vp2px(10)).build());

                labelSeriesContainer.addComponent(label);
            }

            LayoutConfig layoutConfig = new LayoutConfig();
            if (i != 0) {
                layoutConfig.setMarginTop(vp2px(15));
            }

            seriesContainer.addComponent(seriesComponent, layoutConfig);
        }
    }

    private void initPartLayout(Component contentLayout) {
        HomePageTitleComponent titleComponent = (HomePageTitleComponent) contentLayout.findComponentById(ResourceTable.Id_titlePart);
        titleComponent.setData("配件体系课", "查看全部＞", null);

        int itemWidth = (DisplayUtils.getDisplayWidthInPx(getContext()) - vp2px(45)) / 2;
        ArrayList<PartBean> partList = new ArrayList<>();
        partList.add(new PartBean("酷卡侠 电动编程主机 学习课程", new String[]{"实物编程", "编程思维"}, ResourceTable.Media_img_home_part_1));
        partList.add(new PartBean("酷卡侠电机家族学习课程", new String[]{"动力机械", "电动工程"}, ResourceTable.Media_img_home_part_2));

        TableLayout partContainer = (TableLayout) contentLayout.findComponentById(ResourceTable.Id_partContainer);
        for (int i = 0; i < partList.size(); i++) {
            PartBean partBean = partList.get(i);
            Component partComponent = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_layout_home_page_part, partContainer, false);

            Image ivPartCover = (Image) partComponent.findComponentById(ResourceTable.Id_ivPartCover);
            ivPartCover.setCornerRadius(vp2px(5));
            ivPartCover.setPixelMap(partBean.cover);
            ivPartCover.setWidth(itemWidth - vp2px(30));
            ivPartCover.setHeight((itemWidth - vp2px(30)) * 9 / 16);

            DirectionalLayout labelPartContainer = (DirectionalLayout) partComponent.findComponentById(ResourceTable.Id_labelPartContainer);
            for (int j = 0; j < partBean.labels.length; j++) {
                LayoutConfig layoutConfig = new LayoutConfig(LayoutConfig.MATCH_CONTENT, vp2px(20));
                if (j != 0) {
                    layoutConfig.setMarginLeft(vp2px(6));
                }

                Text label = new Text(getContext());
                label.setText(partBean.labels[j]);
                label.setLayoutConfig(layoutConfig);
                label.setTextSize(10, Text.TextSizeType.FP);
                label.setPadding(vp2px(8), 0, vp2px(8), 0);
                label.setTextColor(new Color(Color.getIntColor("#FFA200")));
                label.setBackground(new ShapeElementCreator.Builder().setShape(ShapeElement.RECTANGLE)
                        .setColor(0xFFFFF4D0).setCornerRadiiArray(0, vp2px(10), vp2px(10), vp2px(10)).build());

                labelPartContainer.addComponent(label);
            }

            LayoutConfig layoutConfig = new LayoutConfig(itemWidth, LayoutConfig.MATCH_CONTENT);
            if (i % partContainer.getColumnCount() != 0) {
                layoutConfig.setMarginLeft(vp2px(15));
            }

            partContainer.addComponent(partComponent, layoutConfig);
        }
    }

    private void initEducLayout(Component contentLayout) {
        HomePageTitleComponent titleComponent = (HomePageTitleComponent) contentLayout.findComponentById(ResourceTable.Id_titleEduc);
        titleComponent.setData("亲职教育", "", null);

        ArrayList<EducBean> educList = new ArrayList<>();
        educList.add(new EducBean("【2—3岁】磁力片亲职教育课程", "3.4万", ResourceTable.Media_img_home_educ_1));
        educList.add(new EducBean("【3—4岁】磁力片亲职教育课程", "1.2万", ResourceTable.Media_img_home_educ_2));
        educList.add(new EducBean("【4—5岁】磁力片亲职教育课程", "7.5千", ResourceTable.Media_img_home_educ_3));

        DirectionalLayout educContainer = (DirectionalLayout) contentLayout.findComponentById(ResourceTable.Id_educContainer);
        for (int i = 0; i < educList.size(); i++) {
            EducBean educBean = educList.get(i);
            Component educComponent = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_layout_home_page_educ, educContainer, false);
            educComponent.setWidth((int) (DisplayUtils.getDisplayWidthInPx(getContext()) * 0.6));
            educComponent.setBackground(new ShapeElementCreator.Builder().setShape(ShapeElement.RECTANGLE).setColor(0xffffffff).setCornerRadius(vp2px(5)).build());

            ((Text) educComponent.findComponentById(ResourceTable.Id_tvEducTitle)).setText(educBean.title);
            ((Text) educComponent.findComponentById(ResourceTable.Id_tvEducNum)).setText(educBean.readNum + "人学习过");
            Image ivEducCover = ((Image) educComponent.findComponentById(ResourceTable.Id_ivEducCover));
            ivEducCover.setCornerRadius(vp2px(5));
            ivEducCover.setPixelMap(educBean.cover);

            LayoutConfig layoutConfig = new LayoutConfig();
            if (i == 0) {
                layoutConfig.setMarginLeft(vp2px(15));
            } else if (i == educList.size() - 1) {
                layoutConfig.setMarginRight(vp2px(15));
            }
            if (i != 0) {
                layoutConfig.setMarginLeft(vp2px(8));
            }

            educContainer.addComponent(educComponent, layoutConfig);
        }
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