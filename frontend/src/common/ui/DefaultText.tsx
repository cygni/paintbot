import React from 'react';

interface Props {
  style?: object;
  children?: React.ReactNode;
}

export const DefaultText = ({ style, children }: Props) => <span style={style}>{children}</span>;
