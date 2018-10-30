import * as React from 'react';
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

const SliderInput = styled.input`
  -webkit-appearance: none;
  appearance: none;
  width: 100%;
  height: 7px;
  background: ${(props: InputProps) => props.backgroundColor};
  outline: none;
  opacity: 0.6;
  transform: rotate(${(props: InputProps) => props.rotateDegrees}deg);
`;

export default class Slider extends React.Component<SliderProps, State> {
  public constructor(props: SliderProps) {
    super(props);
    this.sliderChange = this.sliderChange.bind(this);
    this.state = {
      value: this.props.defaultValue,
    };
  }

  public render() {
    const {
      minValue,
      maxValue,
      defaultValue,
      reverse,
      backgroundColor,
    } = this.props;
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

  public currentValue(): number {
    return this.state.value;
  }

  private sliderChange(changeEvent: ChangeEvent<HTMLInputElement>) {
    const value = parseInt(changeEvent.target.value, 10);
    if (!!this.props.sliderChange && !isNaN(value)) {
      this.props.sliderChange(value);
    }
    if (!isNaN(value)) {
      this.setState({
        value,
      });
    }
  }
}
