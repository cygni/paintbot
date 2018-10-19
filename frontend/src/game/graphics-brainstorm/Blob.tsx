import * as React from 'react';
import styled, { keyframes, StyledFunction } from 'styled-components';

interface Props {
  color: string;
  rotation: string;
}

const blobContainer: StyledFunction<Props & React.HTMLProps<HTMLInputElement>> =
  styled.div;

const Container = styled.div`
  height: 5rem;
  width: 5rem;
  position: relative;
`;

const bounce = keyframes`
  from {
    height: 80%;
    width: 80%;
    left: 10%;
    top: 10%;
  }

  50% {
    height: 100%;
    width: 100%;
    left: 0%;
    top: 0%;
  }

  to {
    height: 80%;
    width: 80%;
    left: 10%;
    top: 10%;
  }
`;

const BlobContainer = blobContainer`
  transform: rotate(${props => props.rotation || '0deg'});
  fill: ${props => props.color || 'white'};
  position: absolute;
  animation: ${bounce} .5s linear infinite;
`;

export default class Blob extends React.Component<Props> {
  public render() {
    return (
      <Container>
        <BlobContainer color={this.props.color} rotation={this.props.rotation}>
          <svg x="0px" y="0px" viewBox="0 0 64 64">
            <circle cx="32" cy="32" r="32" />
            <g fill="rgba(0,0,0,0.7)">
              <path d="M19.5,13.6c-1.9,1.1-4.1,0.9-5-0.6c-0.8-1.4,0-3.5,1.9-4.6c1.9-1.1,4.1-0.9,5,0.6C22.3,10.4,21.4,12.5,19.5,13.6z" />
              <path d="M32,61c-4.1,0-8.5-3.8-8.5-8.5c0-4.7,0.5,7.5,8.5,7.5s8.5-12.2,8.5-7.5C40.5,57.2,36.1,61,32,61z" />
              <path d="M49.5,12.9c-0.8,1.5-3,1.8-4.9,0.8c-2-1-2.9-3.1-2.1-4.5c0.8-1.5,3-1.8,4.9-0.8C49.4,9.4,50.3,11.4,49.5,12.9z" />
            </g>
            <g fill="rgba(255,255,255,0.9)">
              <circle cx="32" cy="54.5" r="2.5" />
              <circle cx="32" cy="49.5" r="1.5" />
            </g>
          </svg>
        </BlobContainer>
      </Container>
    );
  }
}
