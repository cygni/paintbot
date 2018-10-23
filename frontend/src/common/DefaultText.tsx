import * as React from 'react';

interface Props {
  style?: object;
  children?: string | undefined | null;
}

export const DefaultText = ({ style, children }: Props) => (
  <span style={style}>{children}</span>
);
