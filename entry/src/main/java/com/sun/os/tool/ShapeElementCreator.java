package com.sun.os.tool;

import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;

public class ShapeElementCreator {

    public static class Builder {
        private ShapeElement shapeElement;

        public Builder() {
            shapeElement = new ShapeElement();
        }

        public Builder(Context context, int xmlId) {
            shapeElement = new ShapeElement(context, xmlId);
        }

        public Builder setShape(int shape) {
            shapeElement.setShape(shape);
            return this;
        }

        public Builder setCornerRadius(float radius) {
            shapeElement.setCornerRadius(radius);
            return this;
        }

        public Builder setCornerRadiiArray(float[] radii) {
            shapeElement.setCornerRadiiArray(radii);
            return this;
        }

        public Builder setCornerRadiiArray(int leftTop, int rightTop, int bottomRight, int bottomLeft) {
            shapeElement.setCornerRadiiArray(new float[]{leftTop, leftTop, rightTop, rightTop, bottomRight, bottomRight, bottomLeft, bottomLeft});
            return this;
        }

        public ShapeElement build() {
            return shapeElement;
        }
    }
}
