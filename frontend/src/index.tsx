import React from 'react';
import ReactDOM from 'react-dom';

import App from './App';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(<App />, document.getElementById('root'));

registerServiceWorker();

declare module 'react' {
  export function lazy<P>(cb: () => Promise<{ default: ComponentType<P> }>): ComponentType<P>;
  export const Suspense: ComponentType<{ fallback?: ReactNode }>;
}
