import * as React from 'react';
import {Route, Switch} from 'react-router-dom'; 
import GameContainer from './game/GameContainer';
import Welcome from './welcome/Welcome';

export const Routes = () => 
    <Switch>
        <Route path={'/'} exact={true} component={Welcome}/>
        <Route path={'/game'} exact={true} component={GameContainer}/>
    </Switch>;