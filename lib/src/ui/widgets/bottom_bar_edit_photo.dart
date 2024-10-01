// Copyright (c) 2021, Christian Betancourt
// https://github.com/criistian14
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file or at
// https://opensource.org/licenses/MIT.

import 'package:document_scanner/src/document_scanner_controller.dart';
import 'package:document_scanner/src/models/filter_type.dart';
import 'package:document_scanner/src/utils/edit_photo_document_style.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

/// Default BottomBar of the Edit Photo page
class BottomBarEditPhoto extends StatelessWidget {
  /// Create a widget with style
  const BottomBarEditPhoto({
    super.key,
    required this.editPhotoDocumentStyle,
  });

  /// The style of the page
  final EditPhotoDocumentStyle editPhotoDocumentStyle;

  @override
  Widget build(BuildContext context) {
    if (editPhotoDocumentStyle.hideBottomBarDefault) {
      return const SizedBox.shrink();
    }

    return Positioned(
      bottom: MediaQuery.of(context).padding.bottom,
      left: 0,
      right: 0,
      child: DecoratedBox(
        decoration: BoxDecoration(
          color: Theme.of(context).primaryColor,
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            // * Natural
            TextButton(
              onPressed: () =>
                  context.read<DocumentScannerController>().applyFilter(
                        FilterType.natural,
                      ),
              style: TextButton.styleFrom(
                foregroundColor: Colors.white,
              ),
              child: const Text(
                'Natural',
              ),
            ),

            // * Gray
            TextButton(
              onPressed: () =>
                  context.read<DocumentScannerController>().applyFilter(
                        FilterType.gray,
                      ),
              style: TextButton.styleFrom(
                foregroundColor: Colors.white,
              ),
              child: const Text(
                'GRAY',
              ),
            ),

            // * ECO
            TextButton(
              onPressed: () =>
                  context.read<DocumentScannerController>().applyFilter(
                        FilterType.eco,
                      ),
              style: TextButton.styleFrom(
                foregroundColor: Colors.white,
              ),
              child: const Text(
                'ECO',
              ),
            ),
          ],
        ),
      ),
    );
  }
}
