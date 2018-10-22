import * as React from 'react';

interface Props {
  style?: object;
  children?: string | undefined | null;
}

export const TextLabel = ({ style, children }: Props) => (
  <div style={style}>{children}</div>
);
