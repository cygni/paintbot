import * as React from 'react';

interface Props {
  num?: number;
  children?:
    | React.ReactElement<any>
    | Array<{} | React.ReactElement<any> | undefined>
    | undefined
    | null;
}

export const Indent = ({ num = 1, children }: Props) => {
  const paddingSize = num * 10;
  return (
    <div style={{ paddingLeft: paddingSize, paddingRight: paddingSize }}>
      {children}
    </div>
  );
};
