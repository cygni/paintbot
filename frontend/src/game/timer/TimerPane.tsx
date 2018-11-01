import React from 'react';
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

export const TimerPane = ({ durationInSeconds, timeInMsPerTick, worldTick }: Props) => (
  <TimerContainer>
    <Timer durationInSeconds={durationInSeconds} timeInMsPerTick={timeInMsPerTick} worldTick={worldTick} />
  </TimerContainer>
);
