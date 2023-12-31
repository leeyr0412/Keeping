import 'package:flutter/material.dart';
import 'package:keeping/util/page_transition_effects.dart';

// 하단 네모난 버튼 클래스
class BottomBtn extends StatelessWidget {
  final String text;
  final Color bgColor;
  final Color textColor;
  final dynamic action;
  final bool isDisabled;
  final bool effect;

  BottomBtn({
    super.key,
    required this.text,
    this.bgColor = const Color(0xFF8320E7),
    this.textColor = Colors.white,
    this.action,
    this.isDisabled = true,
    this.effect = true,
  });

  @override
  Widget build(BuildContext context) {
    return BottomAppBar(
      color: isDisabled ? Colors.black38 : bgColor,
      elevation: 0,
      child: SizedBox(
        height: 70,
        width: double.infinity,
        child: TextButton(
          onPressed: () {
            if (!isDisabled) {
              if (action is Widget) {
                if (effect == true) {
                  Navigator.push(context, MaterialPageRoute(builder: (_) => action));
                } else {
                  noEffectTransition(context, action);
                }
              } else if (action is Function) {
                action();
              } else {
                Navigator.pop(context);
              }
            }
          },
          style: _bottomBtnStyle(textColor),
          child: Text(text),
        ),
      ),
    );
  }
}

// 버튼 스타일
ButtonStyle _bottomBtnStyle(Color textColor) {
  return ButtonStyle(
    backgroundColor: MaterialStateProperty.all<Color>(Colors.transparent),
    foregroundColor: MaterialStateProperty.all<Color>(textColor),
    textStyle: MaterialStateProperty.all<TextStyle>(
      TextStyle(fontSize: 25.0, color: textColor),
    ),
  );
}
