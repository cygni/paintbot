import { FlexDirectionProperty } from 'csstype';
import * as React from 'react';

interface Props {
  children?:
    | React.ReactElement<any>
    | Array<{} | React.ReactElement<any> | undefined>
    | undefined
    | null;
  justifyContent?: string;
  alignItems?: string;
  alignSelf?: string;
  width?: number;
  height?: number;
  style?: object;
}

export class Row extends React.Component<Props> {
  public render() {
    const {
      justifyContent,
      alignItems,
      alignSelf,
      width,
      height,
      style,
      children,
    } = this.props;

    const flexDirection: FlexDirectionProperty = 'row';
    const display = 'flex';

    const viewStyle = {
      flexDirection,
      display,
      justifyContent,
      alignItems,
      alignSelf,
      width,
      height,
      ...style,
    };
    return <div style={viewStyle}>{children}</div>;
  }
}
