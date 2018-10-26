import * as React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { GameScreen } from './game/GameScreen';
import Welcome from './welcome/Welcome';

export const Routes = () => (
  <Router>
    <Switch>
      <Route path={'/'} exact={true} component={Welcome} />
      <Route path={'/game'} exact={true} component={GameScreen} />
    </Switch>
  </Router>
);
