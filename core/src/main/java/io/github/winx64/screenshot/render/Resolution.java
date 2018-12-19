package io.github.winx64.screenshot.render;

import com.google.common.base.Preconditions;

import java.util.*;

public final class Resolution {

    private final int width;
    private final int height;

    public Resolution(Pixels pixels, AspectRatio aspectRatio) {
        Preconditions.checkNotNull(pixels);
        Preconditions.checkNotNull(aspectRatio);

        this.height = pixels.height;
        this.width = (int) Math.round(pixels.height * aspectRatio.ratio);
    }

    public Resolution(int width, int height) {
        Preconditions.checkArgument(width > 0);
        Preconditions.checkArgument(height > 0);

        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public enum Pixels {
        _128P(128, "128p"),
        _144P(144, "144p"),
        _240P(240, "240p"),
        _360P(360, "360p"),
        _480P(480, "480p"),
        _720P(720, "720p"),
        _1080P(1080, "1080p", "fullhd", "full-hd"),
        _4K(2160, "4k", "ultrahd", "ultra-hd"),
        _8K(4320, "8k"),
        _16K(8640, "16k");

        private static final Map<String, Pixels> BY_ALIAS;

        static {
            Map<String, Pixels> byAlias = new HashMap<>();
            for (Pixels pixels : values()) {
                for (String alias : pixels.aliases) {
                    byAlias.put(alias.toLowerCase(), pixels);
                }
            }
            BY_ALIAS = Collections.unmodifiableMap(byAlias);
        }

        private final int height;
        private final List<String> aliases;

        Pixels(int height, String... aliases) {
            this.height = height;
            this.aliases = Collections.unmodifiableList(Arrays.asList(aliases));
        }

        public static Pixels getPixels(String alias) {
            Preconditions.checkNotNull(alias);

            return BY_ALIAS.get(alias.toLowerCase());
        }

        public static Set<String> getAllAliases() {
            return BY_ALIAS.keySet();
        }

        public int getHeight() {
            return height;
        }

        public List<String> getAliases() {
            return aliases;
        }
    }

    public enum AspectRatio {
        _4_3(4 / 3.0, "4:3"),
        _16_9(16 / 9.0, "16:9", "wide", "widescreen", "wide-screen"),
        ULTRAWIDE(21 / 9.0, "21:9", "ultrawide", "ultra-wide");

        private static final Map<String, AspectRatio> BY_ALIAS;

        static {
            Map<String, AspectRatio> byAlias = new HashMap<>();
            for (AspectRatio aspectRatio : values()) {
                for (String alias : aspectRatio.aliases) {
                    byAlias.put(alias.toLowerCase(), aspectRatio);
                }
            }
            BY_ALIAS = Collections.unmodifiableMap(byAlias);
        }

        private final double ratio;
        private final List<String> aliases;

        AspectRatio(double ratio, String... aliases) {
            this.ratio = ratio;
            this.aliases = Collections.unmodifiableList(Arrays.asList(aliases));
        }

        public static AspectRatio getAspectRatio(String alias) {
            Preconditions.checkNotNull(alias);

            return BY_ALIAS.get(alias.toLowerCase());
        }

        public static Set<String> getAllAliases() {
            return BY_ALIAS.keySet();
        }

        public double getRatio() {
            return ratio;
        }

        public List<String> getAliases() {
            return aliases;
        }
    }
}