import 'package:flutter/material.dart';
import 'package:keeping/screens/btn_test/btn_test.dart';
import 'package:keeping/widgets/bottom_modal.dart';
import 'package:keeping/widgets/bottom_nav.dart';
import 'package:keeping/widgets/confirm_btn.dart';
import 'package:keeping/widgets/header.dart';

class Page1 extends StatelessWidget {
  const Page1({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          MyHeader(
            text: '제목',
            elementColor: Colors.black,
            icon: Icon(Icons.arrow_circle_up),
            path: Page1(),
          ),
          ConfirmBtn(text: '확인', path: BtnTest()),
          testBtn(context),
        ],
      ),
      bottomNavigationBar: BottomNav()
    );
  }
}

// 모달 테스트용 - 클릭하면 모달 나옴
Widget testBtn(BuildContext context) {
  return ElevatedButton(
    onPressed: () {
      showModalBottomSheet(
        context: context,
        builder: (BuildContext context) {
          return BottomModal(
            title: '안녕',
            content: testContent(context),
            button: ConfirmBtn(),
          );
        }
      );
    },
    child: const Text('모달'),
  );
}

// 모달 테스트용 - 모달 안에 들어갈 내용 위젯
Widget testContent(BuildContext context) {
  return Center(
    child: Column(
      children: [
        Text('모달에 넣고싶은 내용(위젯들) 넣기'),
        Icon(Icons.heart_broken)
      ],
    ),
  );
}