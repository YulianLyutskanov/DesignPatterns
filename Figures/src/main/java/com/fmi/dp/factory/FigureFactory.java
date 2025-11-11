package com.fmi.dp.factory;

import com.fmi.dp.figures.Figure;

public interface FigureFactory {

    Figure create();

    int countFigures();
}
