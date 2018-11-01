import React from 'react';
import { ChangeEvent } from 'react';
import styled from 'styled-components';

import { StandardColors } from '../Constants';

interface SliderProps {
  sliderChange?: (changeNumber: number) => void;
  maxValue: number;
  minValue: number;
  defaultValue: number;
  reverse?: boolean;
  backgroundColor?: string;
}

interface State {
  value: number;
}

interface InputProps {
  rotateDegrees: number;
  backgroundColor: string;
}

const SliderInput = styled.input<InputProps>`
  -webkit-appearance: none;
  appearance: none;
  width: 100%;
  height: 7px;
  background: ${props => props.backgroundColor};
  outline: none;
  opacity: 0.6;
  transform: rotate(${props => props.rotateDegrees}deg);
`;

export default class Slider extends React.Component<SliderProps, State> {
  private sliderChange = (changeEvent: ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(changeEvent.target.value, 10);
    if (!!this.props.sliderChange && !isNaN(value)) {
      this.props.sliderChange(value);
    }
    if (!isNaN(value)) {
      this.setState({
        value,
      });
    }
  };

  currentValue(): number {
    return this.state.value;
  }

  render() {
    const { minValue, maxValue, defaultValue, reverse, backgroundColor } = this.props;
    const startValue = defaultValue.toString();
    const color = backgroundColor ? backgroundColor : StandardColors.White;
    return (
      <SliderInput
        rotateDegrees={reverse ? 180 : 0}
        backgroundColor={color}
        type={'range'}
        defaultValue={startValue}
        min={minValue}
        max={maxValue}
        step={'10'}
        onChange={this.sliderChange}
      />
    );
  }
}
