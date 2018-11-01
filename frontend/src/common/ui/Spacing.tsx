import React from 'react';

interface Props {
  num?: number;
  children?: React.ReactNode;
}

export const Spacing = ({ num = 1, children }: Props) => {
  const paddingSize = num * 10;
  return <div style={{ paddingTop: paddingSize, paddingBottom: paddingSize }}>{children}</div>;
};
