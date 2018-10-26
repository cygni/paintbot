import * as React from 'react';
import styled from 'styled-components';
import Timer from './Timer';

interface Props {
  durationInSeconds: number;
  timeInMsPerTick: number;
  worldTick: number;
}

const TimerContainer = styled.div`
  display: flex;
`;

export default class TimerPane extends React.Component<Props> {
  public render() {
    const { durationInSeconds, timeInMsPerTick, worldTick } = this.props;
    console.log(this.props);
    return (
      <TimerContainer>
        <Timer
          durationInSeoncds={durationInSeconds}
          timeInMsPerTick={timeInMsPerTick}
          worldTick={worldTick}
          ref={x => {
            if (x !== null) {
              // this.timer = x;
            }
          }}
        />
      </TimerContainer>
    );
  }
}
