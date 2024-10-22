import 'dart:io';
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:document_scanner/document_scanner.dart';


void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<String> _pictures = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {}

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: SingleChildScrollView(
            child: Column(
          children: [
            ElevatedButton(
                onPressed: onPressed, child: const Text("Add Pictures")),
            ElevatedButton(
                onPressed: onSelectPicturesSelect, child: const Text("Select Pictures")),
            for (var picture in _pictures) Image.file(File(picture))
          ],
        )),
      ),
    );
  }

  void onSelectPicturesSelect() async {
    List<String> pictures;
    try {
      pictures = await DocumentScanner.selectDocuments(noOfPages: 15) ?? [];
      print("FROM DART ${pictures[0]}");
      if (!mounted) return;
      setState(() {
        _pictures = pictures;
      });
    } catch (exception) {
      // Handle exception here
    }
  }

  void onPressed() async {
    List<String> pictures;
    try {
      pictures = await DocumentScanner.getPictures(noOfPages: 15, isGalleryImportAllowed: true) ?? [];
      
      if (!mounted) return;
      setState(() {
        _pictures = pictures;
      });
    } catch (exception) {
      // Handle exception here
    }
  }
}
