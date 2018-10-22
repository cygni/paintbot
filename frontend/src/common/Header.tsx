import * as React from 'react';

interface Props {
  label?: string;
}

export const Header = ({ label }: Props) => <h1>{label}</h1>;
