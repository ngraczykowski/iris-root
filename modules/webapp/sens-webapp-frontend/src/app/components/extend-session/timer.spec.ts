import { fakeAsync, tick } from '@angular/core/testing';
import { Timer } from './timer';

describe('Timer', () => {

    let timer: Timer;
    let actions;

    function addRefreshAction(name, time) {
        actions.push('refresh_' + name + '_' + time);
    }

    function addFinishAction(name) {
        actions.push('finish_' + name);
    }

    beforeEach(() => {
        actions = [];
        if (timer) {
            timer.cancel();
        }
        timer = new Timer(100);
    });

    it('should refresh 3 times after running 0.2s timer',
        <any>fakeAsync((): void => {
            timer.start(
                new Date().getTime() + 200,
                (timeLeft) => addRefreshAction('1', timeLeft),
                () => addFinishAction('1'));

            expect(actions).toEqual([]);
            tick(1);
            expect(actions).toEqual(['refresh_1_200']);
            tick(100);
            expect(actions).toEqual(['refresh_1_200', 'refresh_1_100']);
            tick(100);
            expect(actions).toEqual(['refresh_1_200', 'refresh_1_100', 'refresh_1_0', 'finish_1']);
        }));


    it('should not add any action if timer is canceled',
        <any>fakeAsync((): void => {
            timer.start(
                new Date().getTime() + 200,
                (timeLeft) => addRefreshAction('1', timeLeft),
                () => addFinishAction('1'));
            timer.cancel();
            tick(200);
            expect(actions).toEqual([]);
        }));

    it('should refresh 5 times after running 0.2s timer and another 0.4s timer immediately after the first one',
        <any>fakeAsync((): void => {
            timer.start(
                new Date().getTime() + 200,
                (timeLeft) => addRefreshAction('1', timeLeft),
                () => addFinishAction('1'));
            timer.start(
                new Date().getTime() + 400,
                (timeLeft) => addRefreshAction('2', timeLeft),
                () => addFinishAction('2'));

            expect(actions).toEqual([]);
            tick(1);
            expect(actions).toEqual(['refresh_2_400']);
            tick(100);
            expect(actions).toEqual(['refresh_2_400', 'refresh_2_300']);
            tick(100);
            expect(actions).toEqual(['refresh_2_400', 'refresh_2_300', 'refresh_2_200']);
            tick(100);
            expect(actions).toEqual(['refresh_2_400', 'refresh_2_300', 'refresh_2_200', 'refresh_2_100']);
            tick(100);
            expect(actions).toEqual(['refresh_2_400', 'refresh_2_300', 'refresh_2_200', 'refresh_2_100', 'refresh_2_0', 'finish_2']);
        }));

    it('should refresh 7 times after running 0.2s timer, canceling it on second refresh and running another 0.4s timer',
        <any>fakeAsync((): void => {
            timer.start(
                new Date().getTime() + 200,
                (timeLeft) => addRefreshAction('1', timeLeft),
                () => addFinishAction('1'));

            expect(actions).toEqual([]);
            tick(1);
            expect(actions).toEqual([
                'refresh_1_200']);
            tick(100);
            expect(actions).toEqual([
                'refresh_1_200', 'refresh_1_100']);

            timer.start(
                new Date().getTime() + 400,
                (timeLeft) => addRefreshAction('2', timeLeft),
                () => addFinishAction('2'));

            tick(1);
            expect(actions).toEqual([
                'refresh_1_200', 'refresh_1_100',
                'refresh_2_400']);
            tick(100);
            expect(actions).toEqual([
                'refresh_1_200', 'refresh_1_100',
                'refresh_2_400', 'refresh_2_300']);
            tick(100);
            expect(actions).toEqual([
                'refresh_1_200', 'refresh_1_100',
                'refresh_2_400', 'refresh_2_300', 'refresh_2_200']);
            tick(100);
            expect(actions).toEqual([
                'refresh_1_200', 'refresh_1_100',
                'refresh_2_400', 'refresh_2_300', 'refresh_2_200', 'refresh_2_100']);
            tick(100);
            expect(actions).toEqual([
                'refresh_1_200', 'refresh_1_100',
                'refresh_2_400', 'refresh_2_300', 'refresh_2_200', 'refresh_2_100', 'refresh_2_0', 'finish_2']);
        }));

});

