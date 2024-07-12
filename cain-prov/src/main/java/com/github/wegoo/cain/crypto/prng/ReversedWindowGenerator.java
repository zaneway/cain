package com.github.wegoo.cain.crypto.prng;

/**
 * Takes bytes generated by an underling RandomGenerator and reverses the order in
 * each small window (of configurable size).
 * <p>
 * Access to internals is synchronized so a single one of these can be shared.
 * </p>
 */
public class ReversedWindowGenerator
    implements RandomGenerator
{
    private final RandomGenerator generator;

    private byte[] window;
    private int windowCount;

    public ReversedWindowGenerator(
        RandomGenerator generator,
        int             windowSize)
    {
        if (generator == null)
        {
            throw new IllegalArgumentException("generator cannot be null");
        }
        if (windowSize < 2)
        {
            throw new IllegalArgumentException("windowSize must be at least 2");
        }

        this.generator = generator;
        this.window = new byte[windowSize];
    }

    /**
     * Add more seed material to the generator.
     *
     * @param seed a byte array to be mixed into the generator's state.
     */
    public void addSeedMaterial(
        byte[] seed)
    {
        synchronized (this)
        {
            windowCount = 0;
            generator.addSeedMaterial(seed);
        }
    }

    /**
     * Add more seed material to the generator.
     *
     * @param seed a long value to be mixed into the generator's state.
     */
    public void addSeedMaterial(
        long seed)
    {
        synchronized (this)
        {
            windowCount = 0;
            generator.addSeedMaterial(seed);
        }
    }

    /**
     * Fill bytes with random values.
     *
     * @param bytes byte array to be filled.
     */
    public void nextBytes(
        byte[] bytes)
    {
        doNextBytes(bytes, 0, bytes.length);
    }

    /**
     * Fill part of bytes with random values.
     *
     * @param bytes byte array to be filled.
     * @param start index to start filling at.
     * @param len length of segment to fill.
     */
    public void nextBytes(
        byte[]  bytes,
        int     start,
        int     len)
    {
        doNextBytes(bytes, start, len);
    }

    private void doNextBytes(
        byte[]  bytes,
        int     start,
        int     len)
    {
        synchronized (this)
        {
            int done = 0;
            while (done < len)
            {
                if (windowCount < 1)
                {
                    generator.nextBytes(window, 0, window.length);
                    windowCount = window.length;
                }

                bytes[start + done++] = window[--windowCount];
            }
        }
    }
}
