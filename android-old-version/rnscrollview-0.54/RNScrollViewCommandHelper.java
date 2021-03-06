/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.taumu.rnscrollview;

import javax.annotation.Nullable;

import java.util.Map;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.common.MapBuilder;


public class RNScrollViewCommandHelper {

  public static final int COMMAND_SCROLL_TO = 1;
  public static final int COMMAND_SCROLL_TO_END = 2;
  public static final int COMMAND_FLASH_SCROLL_INDICATORS = 3;
  public static final int COMMAND_SET_SCROLL_ENABLED = 4;

  public interface ScrollCommandHandler<T> {
    void scrollTo(T scrollView, ScrollToCommandData data);
    void scrollToEnd(T scrollView, ScrollToEndCommandData data);
    void flashScrollIndicators(T scrollView);
    void setRNScrollEnabled(T scrollView, boolean data);
  }

  public static class ScrollToCommandData {

    public final int mDestX, mDestY;
    public final boolean mAnimated;

    ScrollToCommandData(int destX, int destY, boolean animated) {
      mDestX = destX;
      mDestY = destY;
      mAnimated = animated;
    }
  }

  public static class ScrollToEndCommandData {

    public final boolean mAnimated;

    ScrollToEndCommandData(boolean animated) {
      mAnimated = animated;
    }
  }

  public static Map<String,Integer> getCommandsMap() {
    return MapBuilder.of(
        "scrollTo",
        COMMAND_SCROLL_TO,
        "scrollToEnd",
        COMMAND_SCROLL_TO_END,
        "flashScrollIndicators",
        COMMAND_FLASH_SCROLL_INDICATORS,
        "setScrollEnabled",
        COMMAND_SET_SCROLL_ENABLED);
  }

  public static <T> void receiveCommand(
      ScrollCommandHandler<T> viewManager,
      T scrollView,
      int commandType,
      @Nullable ReadableArray args) {
    Assertions.assertNotNull(viewManager);
    Assertions.assertNotNull(scrollView);
    Assertions.assertNotNull(args);
    switch (commandType) {
      case COMMAND_SCROLL_TO: {
        int destX = Math.round(PixelUtil.toPixelFromDIP(args.getDouble(0)));
        int destY = Math.round(PixelUtil.toPixelFromDIP(args.getDouble(1)));
        boolean animated = args.getBoolean(2);
        viewManager.scrollTo(scrollView, new ScrollToCommandData(destX, destY, animated));
        return;
      }
      case COMMAND_SCROLL_TO_END: {
        boolean animated = args.getBoolean(0);
        viewManager.scrollToEnd(scrollView, new ScrollToEndCommandData(animated));
        return;
      }
      case COMMAND_FLASH_SCROLL_INDICATORS:
        viewManager.flashScrollIndicators(scrollView);
        return;
      case COMMAND_SET_SCROLL_ENABLED: {
        boolean isEnable = args.getBoolean(0);
        viewManager.setRNScrollEnabled(scrollView, isEnable);
        return;
      }
      default:
        throw new IllegalArgumentException(String.format(
            "Unsupported command %d received by %s.",
            commandType,
            viewManager.getClass().getSimpleName()));
    }
  }
}
